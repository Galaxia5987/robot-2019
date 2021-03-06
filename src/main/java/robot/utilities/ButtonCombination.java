package robot.utilities;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * Extension of the button class, triggers a command when all of the buttons are pressed simultaneously
 *
 * @author paulo 
 */
public class ButtonCombination extends Button {

    private final GenericHID m_joystick;
    private final int[] buttons;
    /**
     * Create a joystick button for triggering commands.
     *
     * @param joystick     The GenericHID object that has the button (e.g. Joystick, KinectStick,
     *                     etc)
     * @param buttons The button IDS  which need to all be pressed (see {@link GenericHID#getRawButton(int) }
     */
    public ButtonCombination(GenericHID joystick, int ... buttons) {
        this.buttons = buttons;
        m_joystick = joystick;
    }
    @Override
    public boolean get() {
        boolean arePressed = true;
        for (int button : buttons){
            arePressed = arePressed && m_joystick.getRawButton(button);
        }
        return arePressed;
    }
}
