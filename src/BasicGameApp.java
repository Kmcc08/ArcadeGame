//Basic Game Application
//Version 2
// Basic Object, Image, Movement
// basketnaut moves to the right.
// Threaded

//K. Chun 8/2018

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


//*******************************************************************************
// Class Definition Section

// step 1 add KeyListener to the implements
public class BasicGameApp implements Runnable, KeyListener {

	//Variable Definition Section
	//Declare the variables used in the program
	//You can set their initial values too

	//Sets the width and height of the program window
	final int WIDTH = 1000;
	final int HEIGHT = 700;

	//Declare the variables needed for the graphics
	public JFrame frame;
	public Canvas canvas;
	public JPanel panel;

	public BufferStrategy bufferStrategy;
	public Image basketPic;

	//Declare the objects used in the program
	//These are things that are made up of more than one variable type
	private Basket basket;
	private Basket apple;

	Basket[]  appleArray = new Basket[10];

	Basket[]  wrapArray = new Basket[2];
	private Image backgroundPic;
	private Image applePic;
	appleArray[0].isAlive = true;
	private Image greenApplePic;
	//private Image basketPic;


	// Main method definition
	// This is the code that runs first and automatically
	public static void main(String[] args) {
		BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
		new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
	}


	// Constructor Method
	// This has the same name as the class
	// This section is the setup portion of the program
	// Initialize your variables and construct your program objects here.
	public BasicGameApp() {

		setUpGraphics();

		//variable and objects
		//create (construct) the objects needed for the game and load up
		basketPic = Toolkit.getDefaultToolkit().getImage("basket.jpg");
		applePic = Toolkit.getDefaultToolkit().getImage("apple.jpg");
		backgroundPic = Toolkit.getDefaultToolkit().getImage("trees.jpg");//load the picture
		greenApplePic =  Toolkit.getDefaultToolkit().getImage("greenApple.jpg");
		basket = new Basket(10,100);
		apple = new Basket(600,600);


//apple.height = 100;
		for(int x = 0; x<appleArray.length;x++){
			appleArray[x] = new Basket((int)(Math.random()*900),(int)(Math.random()*600));
			appleArray[x].dx = (int)(Math.random()*9);
			appleArray[x].dy = (int)(Math.random()*8);
		}
		for(int x=0; x< wrapArray.length; x++){
			wrapArray[x] = new Basket((int)(Math.random()*700),(int)(Math.random()*500));
			wrapArray[x].dx = (int)(Math.random()*8);
			wrapArray[x].dy = (int)(Math.random()*9);
		}
		//for loop for wrapArray


	}// BasicGameApp()


//*******************************************************************************
//User Method Section
//
// put your code to do things here.

	// main thread
	// this is the code that plays the game after you set things up
	public void run() {

		//for the moment we will loop things forever.
		while (true) {

			moveThings();  //move all the game objects
			render();  // paint the graphics
			pause(20); // sleep for 10 ms
		}
	}


	public void moveThings()
	{
		//calls the move( ) code in the objects
		basket.move();
		basket.wrap();
		collisions();
		for(int y=0; y< appleArray.length; y++){
			appleArray[y].bounce();
		}
		for(int y=0; y<wrapArray.length; y++){
			wrapArray[y].wrap();
		}


	}
	public void collisions(){




		for(int b = 0; b< appleArray.length; b++){
			if(basket.rec.intersects(appleArray[b].rec)){
				System.out.println("crashing");
				appleArray[0].isAlive = false;
			}
		}


		for(int b = 0; b< wrapArray.length; b++){
			if(basket.rec.intersects(wrapArray[b].rec)){
				System.out.println("crashing");
			}
		}

	}

	//Pauses or sleeps the computer for the amount specified in milliseconds
	public void pause(int time ){
		//sleep
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {

		}
	}


	//Graphics setup method
	private void setUpGraphics() {
		frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

		panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
		panel.setLayout(null);   //set the layout

		// creates a canvas which is a blank rectangular area of the screen onto which the application can draw
		// and trap input events (Mouse and Keyboard events)
		canvas = new Canvas();
		canvas.setBounds(0, 0, WIDTH, HEIGHT);
		canvas.setIgnoreRepaint(true);
// step 2 add keylistner to canvas as this
		canvas.addKeyListener(this);

		panel.add(canvas);  // adds the canvas to the panel.

		// frame operations
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
		frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
		frame.setResizable(false);   //makes it so the frame cannot be resized
		frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

		// sets up things so the screen displays images nicely.
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		canvas.requestFocus();
		System.out.println("DONE graphic setup");

	}


	//paints things on the screen using bufferStrategy
	private void render() {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);

		//draw the image of the basketnaut
		g.drawImage(backgroundPic,0, 0, WIDTH, HEIGHT, null);
		g.drawImage(basketPic, basket.xpos, basket.ypos, basket.width, basket.height, null);
		if (appleArray[0].isAlive == true) {
			g.drawImage(applePic, apple.xpos, apple.ypos, apple.width, apple.height, null);
		}
		g.drawImage(greenApplePic,apple.xpos, apple.ypos, apple.width, apple.height, null);

		for(int l = 0; l < appleArray.length; l++){
			System.out.println("xpos " +appleArray[l].xpos);
		//	System.out.println("ypos " +appleArray[l].ypos);

			g.drawImage(applePic, appleArray[l].xpos, appleArray[l].ypos, appleArray[l].width, appleArray[l].height, null);
		}
		for(int e = 0; e< wrapArray.length; e++){
			g.drawImage(greenApplePic, wrapArray[e].xpos, wrapArray[e].ypos, wrapArray[e].width, wrapArray[e].height, null);
		}
		g.dispose();

		bufferStrategy.show();

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyChar());
		System.out.println(e.getKeyCode());
		if (e.getKeyCode() == 38){

			basket.dy = -5;
			basket.dx = 0;
		}
		if (e.getKeyCode() == 40){

			basket.dy = 5;
			basket.dx = 0;
		}
		if (e.getKeyCode() == 39){;

			basket.dx = 5;
			basket.dy = 0;

		}
		if (e.getKeyCode() == 37){

			basket.dx = -5;
			basket.dy = 0;
		}
		if(e.getKeyCode() == 16){

			basket.dy = -5;
			basket.dx =-5;
		}
	}

	// hw: identify key codes for up down left and right arrow keys
	@Override
	public void keyReleased(KeyEvent e) {

		System.out.println(e.getKeyChar());
		System.out.println(e.getKeyCode());
		if(e.getKeyCode() == 38){
			basket.up = false;
		}
		if(e.getKeyCode() == 40){
			basket.down = false;
		}
		if(e.getKeyCode() == 37){
			basket.left = false;
		}
		if(e.getKeyCode() == 38){
			basket.right = false;
		}


	}

	// ad step 3 add methods keyReleased, keyPressed, and keyType
}