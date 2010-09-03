package com.syncleus.dann;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

public class ComponentUnavailableException extends Exception {

    private final UnsatisfiedLinkError unsatisfiedLinkError;

    public ComponentUnavailableException(UnsatisfiedLinkError e) {
        super(e.getMessage());
        this.unsatisfiedLinkError = e;
    }

    /**
     * creates a JComponent (usually a JPanel) that provides instructions for to fix or activate the missing component.
     * @return Swing component
     */
    public JComponent newPanel() {
        if (unsatisfiedLinkError != null) {
            if (unsatisfiedLinkError.getMessage().contains("j3d")) {
                JPanel p = new JPanel(new BorderLayout());
                String msg = "<span style=\"font-size: 20pt\"><b>Java Component Missing:</b> " + this.toString() + "</span><br/><br/>";
                msg += "See <a href=\"http://www.oracle.com/technetwork/java/javase/tech/index-jsp-138252.html\">http://www.oracle.com/technetwork/java/javase/tech/index-jsp-138252.html</a> for Java3D installation instructions</a>.";
                JTextPane msgArea = new JTextPane();
                int bS = 6;
                msgArea.setEditable(false);
                msgArea.setOpaque(false);
                msgArea.setContentType("text/html");
                msgArea.setBorder(new EmptyBorder(bS, bS, bS, bS));
                msgArea.setText("<html> " + msg + " </html>");
                p.add(msgArea, BorderLayout.CENTER);
                return p;
            }
            return new JLabel(unsatisfiedLinkError.toString());
        }
        return new JLabel(this.toString());
    }
}
