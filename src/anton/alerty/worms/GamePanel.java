package anton.alerty.worms;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {

	public static final int PWIDTH= 500;
	public static final int PHEIGHT = 400;
	
	private Thread animator;
	
	public volatile boolean running = false; //stops the animation
	public volatile boolean gameOver = false; //for game termination
	public volatile boolean isPaused = false; //for game pause
	
	private Graphics dbg;
	private Image dbImage = null;
	
	public GamePanel() {
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
		
		setFocusable(true);
		requestFocus();
		readyForTermination();
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				testPress(e.getX(), e.getY());
			}
		});
	
	}
	
	public void addNotify() {
		super.addNotify();
		startGame();
	}
	
	public void startGame() {
		if ((animator == null) || (!gameOver)) {
			animator = new Thread(this);
			animator.start();
		}
	}

	public void stopGame() {
		running = false;
	}
	
	public void run() {
		running = true;
		while (running) {
			gameUpdate();
			gameRender();
			paintScreen();
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException ex)  {
				ex.printStackTrace();
			}
		}
		System.exit(0);
	}
	
	public void gameUpdate() {
		if (!isPaused && !gameOver) {
			
		}
	}
	
	public void gameRender() {
		if (dbImage == null) {
			dbImage = createImage(PWIDTH, PHEIGHT);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			} else dbg = dbImage.getGraphics();
		} 
		
		dbg.setColor(Color.white);
		dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
		
		//draw game elements
		//
		
		if (gameOver) {
			gameOverMessage(dbg);
		}
	}

	public void paintScreen() {
		
		Graphics g;
		
		try {
			g = this.getGraphics();
			if ((g != null) && (dbImage != null)) {
				g.drawImage(dbImage, 0, 0, null);
			}
			Toolkit.getDefaultToolkit().sync();
			g.dispose();
		} catch (Exception e) {
			System.out.println("Graphics context error: " + e);
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (dbImage != null)  {
			g.drawImage(dbImage, 0, 0, null);
		}
	}
	
	private void gameOverMessage(Graphics g) {
		//center game-over message
		g.drawString(msg, x, y);
	}
	
	private void readyForTermination() {
		addKeyListener(new KeyAdapter() {
		// listen for esq, q, end, ctrl-c	
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if ((keyCode == KeyEvent.VK_ESCAPE) ||
				    (keyCode == KeyEvent.VK_Q) ||
				    (keyCode == KeyEvent.VK_END) ||
				    ((keyCode == KeyEvent.VK_C) && (e.isControlDown()))) {
				  running = false;	
				}
			}
		});
	}
	
	private void testPress(int x, int y) {
		if (!isPaused && !gameOver) {
			//do smth with x, y;
		}
	}
	
}
