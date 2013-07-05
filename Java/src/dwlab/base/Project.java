/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.base;

import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import java.util.LinkedList;

/**
 * Class for main project and subprojects.
 */
public class Project extends Obj {
	public static int collisionChecks;
	public static int tilesDisplayed;
	public static int spritesDisplayed;
	public static int spritesActed;
	public static boolean spriteActed;

	public static Project current;
	public static Sprite cursor = new Sprite();
	public static LinkedList<Obj> managers = new LinkedList<Obj>();
	

	/**
	* Quantity of logic frames per second.
	* @see #logic
	*/
	public static double logicFPS = 75;
	public static double deltaTime;

	public static int maxLogicStepsWithoutRender = 6;

	/**
	* Current frames per second quantity.
	* @see #render
	*/
	public static int fPS;

	/**
	* Flipping flag.
	* If set to True then screen clearing will be performed before Render() and buffer switching will be performed after Render().
	*/
	public static boolean flipping = true;

	/**
	 * Current logic frame number.
	 */
	public int pass;

	/**
	 * Current game time in seconds (starts from 0).
	 * @see #perSecond
	 */
	public double time = 0.0;

	/**
	 * Exit flag.
	 * Set it to True to exit project.
	 */
	public boolean exiting;

	public long startingTime;

	// ==================== Management ===================	

	/**
	 * Rendering method.
	 * Fill it with objects drawing commands. Will be executed as many times as possible, while keeping logic frame rate.
	 * 
	 * @see #minFPS, #fPS
	 */
	public void render() {
	}


	/**
	 * Logic method. 
	 * Fill it with project mechanics commands. Will be executed "LogicFPS" times per second.
	 * 
	 * @see #logicFPS
	 */
	public void logic() {
	}


	/**
	 * Deinitialization method.
	 * It will be executed before exit from the project.
	 * 
	 * @see #init, #initGraphics, #initSound
	 */
	public void deInit() {
	}

	
	public static ButtonAction exitButton = ButtonAction.create( KeyboardKey.create( Key.ESCAPE ) );

	/**
	 * Executes the project.
	 * You cannot use this method to execute more projects if the project is already running, use Insert() method instead.
	 */
	@Override
	public void act() {
		Project oldProject = current;
		current = this;

		Sys.flushControllers();

		exiting = false;
		pass = 1;
		deltaTime = 0;

		init();

		time = 0.0;
		startingTime = System.currentTimeMillis();

		double realTime;
		int fPSCount = 0;
		long fPSTime = 0l;
		
		int logicStepsWithoutRender = 0;

		for( Obj manager : managers ) manager.act();
		managers.clear();
			
		while( true ) {
			deltaTime = 1.0 / logicFPS;
			time += deltaTime;

			collisionChecks = 0;
			spritesActed = 0;

			processEvents();

			cursor.setMouseCoords();
			logic();
			for( Obj manager : managers ) {
				manager.act();
			}
			managers.clear();
			
			if( exiting ) break;

			logicStepsWithoutRender += 1;

			while( true ) {
				realTime = 0.001 * ( System.currentTimeMillis() - startingTime );
				if( realTime >= time && logicStepsWithoutRender <= maxLogicStepsWithoutRender ) break;

				if( flipping && Graphics.initialized() ) Graphics.clearScreen();

				spritesDisplayed = 0;
				tilesDisplayed = 0;

				Camera.current.setCameraViewport();
				cursor.setMouseCoords();
				render();

				if( flipping && Graphics.initialized() ) Graphics.swapBuffers();

				logicStepsWithoutRender = 0;
				fPSCount ++;
			}

			if( System.currentTimeMillis() >= 1000 + fPSTime ) {
				fPS = fPSCount;
				fPSCount = 0;
				fPSTime = System.currentTimeMillis();
			}

			pass += 1;
		}

		deInit();
		current = oldProject;
	}

	// ==================== Events ===================		

	public void processEvents() {
		Sys.processEvents( this );
		if( exitButton != null ) if( exitButton.wasPressed() ) exiting = true;
	}
	

	public void onKeyboardEvent() {
	}
	

	public void onMouseEvent() {
	}


	public void onCloseButton() {
		exiting = true;
	}


	public void onWindowResize() {
	}

	// ==================== Other ===================	

	/**
	 * Switches the project execution to given.
	 * Use this command instead of calling another Execute() method.
	 * 
	 * @see #lTButtonAction example
	 */
	public void switchTo( Project project ) {
		long freezingTime = System.currentTimeMillis();
		project.act();
		deltaTime = 1.0 / logicFPS;
		startingTime += System.currentTimeMillis() - freezingTime;
	}


	/**
	* Draws various debugging information on screen.
	* See also #wedgeOffWithSprite example
	*/
	public static void showDebugInfo() {
		printText( "FPS: " + fPS );
		printText( "Memory: " + Runtime.getRuntime().totalMemory() / 1024 + "kb", 1 );

		printText( "Collision  checks: " + collisionChecks, 2 );
		printText( "Tiles  displayed: " + tilesDisplayed, 3 );
		printText( "Sprites  displayed: " + spritesDisplayed, 4 );
		printText( "Sprites  acted: " + spritesActed, 5 );
	}
}
