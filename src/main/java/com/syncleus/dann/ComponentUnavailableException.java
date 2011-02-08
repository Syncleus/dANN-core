package com.syncleus.dann;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

public class ComponentUnavailableException extends Exception
{
	private static final int BORDER_SPACE = 6;
	private final UnsatisfiedLinkError unsatisfiedLinkError;

	public ComponentUnavailableException(final UnsatisfiedLinkError error)
	{
		super(error.getMessage());
		this.unsatisfiedLinkError = error;
	}

	/**
	 * Creates a JComponent (usually a JPanel) that provides instructions for to
	 * fix or activate the missing component.
	 * @return Swing component
	 */
	public JComponent newPanel()
	{
		JComponent component;

		if (unsatisfiedLinkError == null)
		{
			component = new JLabel(this.toString());
		}
		else
		{
			if (unsatisfiedLinkError.getMessage().contains("j3d"))
			{
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
			else
			{
				component = new JLabel(unsatisfiedLinkError.toString());
			}
		}

		return component;
	}
}
