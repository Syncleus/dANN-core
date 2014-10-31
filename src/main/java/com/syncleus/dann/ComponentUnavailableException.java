/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ComponentUnavailableException extends Exception {
    private static final int BORDER_SPACE = 6;
    private static final long serialVersionUID = 2556551692305248225L;
    private final UnsatisfiedLinkError unsatisfiedLinkError;

    public ComponentUnavailableException(final UnsatisfiedLinkError error) {
        super(error);
        this.unsatisfiedLinkError = error;
    }

    /**
     * Creates a JComponent (usually a JPanel) that provides instructions for to
     * fix or activate the missing component.
     *
     * @return Swing component
     */
    public JComponent newPanel() {
        JComponent component;

        if (this.unsatisfiedLinkError == null) {
            component = new JLabel(this.toString());
        }
        else {
            if (this.unsatisfiedLinkError.getMessage().contains("j3d")) {
                final JPanel panel = new JPanel(new BorderLayout());
                final StringBuilder msg = new StringBuilder(
                                                                   "<html> <span style=\"font-size: 20pt\"><b>"
                                                                           + "Java Component Missing:</b> ");
                msg.append(this.toString()).append("</span><br/><br/>See"
                                                           + " <a href=\""
                                                           + "http://www.oracle.com/technetwork/java/javase/tech/index-jsp-138252.html\">"
                                                           + "http://www.oracle.com/technetwork/java/javase/tech/index-jsp-138252.html</a>"
                                                           + " for Java3D installation instructions</a>. </html>");
                final JTextPane msgArea = new JTextPane();
                msgArea.setEditable(false);
                msgArea.setOpaque(false);
                msgArea.setContentType("text/html");
                msgArea.setBorder(new EmptyBorder(BORDER_SPACE, BORDER_SPACE,
                                                         BORDER_SPACE, BORDER_SPACE));
                msgArea.setText(msg.toString());
                panel.add(msgArea, BorderLayout.CENTER);
                component = panel;
            }
            else {
                component = new JLabel(this.unsatisfiedLinkError.toString());
            }
        }

        return component;
    }
}
