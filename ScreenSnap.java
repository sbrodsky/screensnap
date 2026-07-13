import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenSnap {
    // Keep a reference to the system tray icon so we can show notifications later
    private static TrayIcon trayIcon = null;

    // Track modifier key states for CTRL-ALT-K hotkey
    private static boolean ctrlPressed = false;
    private static boolean altPressed = false;

    public static void main(String[] args) throws Exception {
        System.setProperty("apple.awt.UIElement", "true");

        SwingUtilities.invokeLater(() -> startWithGlobalHotkey());
    }

    private static void startWithGlobalHotkey() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            JOptionPane.showMessageDialog(null,
                    "Failed to register global hook:\n" + ex.getMessage(),
                    "ScreenSnap Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return;
        }

        // Optional tray icon
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(
                    createTrayImage(),
                    "ScreenSnap (CTRL-ALT-K to capture)",
                    new PopupMenu() {{
                        add(new MenuItem("Exit") {{
                            addActionListener(e -> System.exit(0));
                        }});
                    }}
            );
            trayIcon.addActionListener(e -> showSelectionOverlay());
            try {
                tray.add(trayIcon);
            } catch (AWTException ex) {
                JOptionPane.showMessageDialog(null,
                        "Failed to add tray icon:\n" + ex.getMessage(),
                        "ScreenSnap Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        // Global hotkey listener for CTRL-ALT-K
        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
                    ctrlPressed = true;
                } else if (e.getKeyCode() == NativeKeyEvent.VC_ALT) {
                    altPressed = true;
                } else if (e.getKeyCode() == NativeKeyEvent.VC_K && ctrlPressed && altPressed) {
                    showSelectionOverlay();
                }
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent e) {
                if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
                    ctrlPressed = false;
                } else if (e.getKeyCode() == NativeKeyEvent.VC_ALT) {
                    altPressed = false;
                }
            }

            @Override public void nativeKeyTyped(NativeKeyEvent e) {}
        });
    }

    private static Image createTrayImage() {
        int size = 16;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(60, 120, 200));
        g2.fillOval(1, 1, size - 2, size - 2);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRect(3, 3, size - 7, size - 7);
        g2.dispose();
        return img;
    }

    private static void showSelectionOverlay() {
        try {
            Robot robot = new Robot();
            Rectangle virtualBounds = getVirtualScreenBounds();
            BufferedImage fullScreen = robot.createScreenCapture(virtualBounds);

            SelectionOverlay overlay = new SelectionOverlay(fullScreen, virtualBounds, () -> {
                // Nothing special needed when closed
            });
            overlay.show();
        } catch (AWTException e) {
            JOptionPane.showMessageDialog(null,
                    "Error preparing selection overlay:\n" + e.getMessage(),
                    "ScreenSnap Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static Rectangle getVirtualScreenBounds() {
        Rectangle bounds = new Rectangle();
        for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            bounds = bounds.union(gd.getDefaultConfiguration().getBounds());
        }
        return bounds;
    }

    // -----------------------------------------------------------------------
    // Overlay window (same as before)
    // -----------------------------------------------------------------------
    static class SelectionOverlay extends JWindow {

        private final BufferedImage screenshot;
        private final Rectangle virtualBounds;
        private final Runnable onClosed;

        private Point startPoint;
        private Rectangle selection;

        private static final double ASPECT_RATIO = 3.0 / 4.0; // or 0.74

        SelectionOverlay(BufferedImage screenshot, Rectangle virtualBounds, Runnable onClosed) {
            this.screenshot    = screenshot;
            this.virtualBounds = virtualBounds;
            this.onClosed      = onClosed;

            setAlwaysOnTop(true);
            setBounds(virtualBounds);

            JPanel canvas = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(screenshot, 0, 0, null);

                    if (selection != null) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(new Color(0, 0, 0, 120));

                        int sw = getWidth(), sh = getHeight();
                        int sx = selection.x, sy = selection.y,
                                sw2 = selection.width, sh2 = selection.height;

                        g2.fillRect(0,       0,       sw,       sy);
                        g2.fillRect(0,       sy,      sx,       sh2);
                        g2.fillRect(sx+sw2,  sy,      sw-sx-sw2, sh2);
                        g2.fillRect(0,       sy+sh2,  sw,       sh-sy-sh2);

                        g2.setColor(Color.WHITE);
                        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER, 1, new float[]{6, 3}, 0));
                        g2.draw(selection);
                        g2.dispose();
                    }
                }
            };

            canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

            MouseAdapter ma = new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) { dispose(); return; }
                    startPoint = e.getPoint();
                    selection  = null;
                }

                @Override public void mouseDragged(MouseEvent e) {
                    selection = normalizeRectWithAspectRatio(startPoint, e.getPoint(), canvas.getBounds());
                    canvas.repaint();
                }

                @Override public void mouseReleased(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) return;
                    if (selection != null && selection.width > 2 && selection.height > 2) {
                        dispose();
                        captureAndSave(selection);
                    }
                }
            };

            canvas.addMouseListener(ma);
            canvas.addMouseMotionListener(ma);

            canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
            canvas.getActionMap().put("cancel", new AbstractAction() {
                @Override public void actionPerformed(ActionEvent e) { dispose(); }
            });

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    if (onClosed != null) onClosed.run();
                }
            });

            setContentPane(canvas);
        }

        private Rectangle normalizeRectWithAspectRatio(Point a, Point b, Rectangle canvasBounds) {
            int dx = b.x - a.x;
            int dy = b.y - a.y;

            int w, h, x, y;

            if (Math.abs(dx) > Math.abs(dy) * ASPECT_RATIO) {
                w = (int) Math.signum(dx) * Math.abs(dx);
                h = (int) Math.signum(dy) * (int) (Math.abs(w) / ASPECT_RATIO);
            } else {
                h = (int) Math.signum(dy) * Math.abs(dy);
                w = (int) Math.signum(dx) * (int) (Math.abs(h) * ASPECT_RATIO);
            }

            x = a.x;
            y = a.y;

            if (w < 0) { x += w; w = -w; }
            if (h < 0) { y += h; h = -h; }

            if (x < canvasBounds.x) {
                int diff = canvasBounds.x - x;
                x += diff;
                w -= diff;
            }
            if (y < canvasBounds.y) {
                int diff = canvasBounds.y - y;
                y += diff;
                h -= diff;
            }
            if (x + w > canvasBounds.x + canvasBounds.width) {
                w = (canvasBounds.x + canvasBounds.width) - x;
            }
            if (y + h > canvasBounds.y + canvasBounds.height) {
                h = (canvasBounds.y + canvasBounds.height) - y;
            }

            if (w < 1) w = 1;
            if (h < 1) h = 1;

            double ratio = (double) w / h;
            if (ratio > ASPECT_RATIO) {
                w = (int) (h * ASPECT_RATIO);
            } else {
                h = (int) (w / ASPECT_RATIO);
            }

            return new Rectangle(x, y, w, h);
        }

        private void captureAndSave(Rectangle sel) {
            Rectangle screenRect = new Rectangle(
                    virtualBounds.x + sel.x,
                    virtualBounds.y + sel.y,
                    sel.width,
                    sel.height
            );

            try {
                Robot robot = new Robot();
                BufferedImage capture = robot.createScreenCapture(screenRect);

                String ts = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String desktop = System.getProperty("user.home") + File.separator + "Desktop";
                File out = new File(desktop, "ScreenSnap_" + ts + ".png");

                ImageIO.write(capture, "PNG", out);

                if (SystemTray.isSupported() && trayIcon != null) {
                    // Use the existing tray icon to display a non-blocking notification
                    trayIcon.displayMessage("ScreenSnap", "Screenshot saved:\n" + out.getName(), TrayIcon.MessageType.INFO);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Saved to:\n" + out.getAbsolutePath(),
                            "ScreenSnap", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Error saving screenshot:\n" + ex.getMessage(),
                        "ScreenSnap", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}