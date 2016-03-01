/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

/**
 * Wrapper/convenience class for the Xbox360 gamepads that MetalCow has
 */

// TODO: Unify/make this dropinabble with AIRFLOController

public class Xbox360Controller extends CowGamepad{
	
	public static final int		AXIS_LX			= 0;
	
	public static final int		AXIS_LY			= 1;

	/**
	 * The primary Xbox 360 controller left trigger. Values range from 0.0 to
	 * 1.0.
	 */

	public static final int		AXIS_LT		= 2;
	/**
	 * The primary Xbox 360 controller right trigger. Values range from 0.0 to
	 * 1.0.
	 */

	public static final int		AXIS_RT		= 3;
	/**
	 * The primaryXbox 360 controller right X-axis.
	 */
	public static final int		AXIS_RX			= 4;
	/**
	 * The primary Xbox 360 controller right Y-axis.
	 */
	public static final int		AXIS_RY			= 5;

	
	/**
	 * The driver Xbox 360 controller A (south) button. See {@link http
	 * ://www.chiefdelphi.com/forums/showpost.php?p=1003245&postcount=8}.
	 */
	public static final int		BTN_A				= 1;
	/**
	 * The primary Xbox 360 controller B (east) button.
	 */
	public static final int		BTN_B				= 2;
	/**
	 * The primary Xbox 360 controller X (west) button.
	 */
	public static final int		BTN_X				= 3;
	/**
	 * The primary Xbox 360 controller Y (north) button.
	 */
	public static final int		BTN_Y				= 4;
	/**
	 * The primary Xbox 360 controller left bumper.
	 */
	public static final int		BTN_LBUMP			= 5;
	/**
	 * The primary Xbox 360 controller right bumper.
	 */
	public static final int		BTN_RBUMP			= 6;
	/**
	 * The primary Xbox 360 controller and Guitar Hero X-plorer back button.
	 */
	public static final int		BTN_BACK			= 7;
	/**
	 * The primary Xbox 360 controller and Guitar Hero X-plorer start button.
	 */
	public static final int		BTN_START			= 8;
	/**
	 * The primary Xbox 360 controller left joystick (pressed).
	 */
	public static final int		BTN_LSTICK			= 9;
	/**
	 * The primary Xbox 360 controller right joystick (pressed).
	 */
	public static final int		BTN_RSTICK			= 10;
	
	public Xbox360Controller(int port) {
		super(port);	
	}
	
	public double getLY(){
		return getRawAxis(AXIS_LY);
	}
	
	public double getLX(){
		return -getRawAxis(AXIS_LX);
	}
	
	public double getRY() {
		return -getRawAxis(AXIS_RY);
	}
	public double getRX(){
		return getRawAxis(AXIS_RX);
	}
	public double getRT() {
		return getRawAxis(AXIS_RT);
	}
	public double getLT(){
		return getRawAxis(AXIS_LT);
	}
	
}