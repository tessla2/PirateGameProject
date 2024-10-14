package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class GameWindow {
	private JFrame jframe;

	public GameWindow(GamePanel gamePanel) {
		jframe = new JFrame();

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// terminate the program on the close button
		jframe.add(gamePanel); // I add the frame
		jframe.setResizable(false);
		jframe.pack();
		jframe.setLocationRelativeTo(null);
		jframe.setVisible(true); // turn visible

		jframe.addWindowFocusListener(new WindowFocusListener() { // recieve events from window

			@Override
			public void windowLostFocus(WindowEvent e) { // Invoked when the Window is no longer the focused Window,
															// which meansthat keyboard events will no longer be
															// delivered to the Window or any ofits subcomponents.
				gamePanel.getGame().windowFocusLost();
			}

			@Override
			public void windowGainedFocus(WindowEvent e) {

			}
		});

	}
}