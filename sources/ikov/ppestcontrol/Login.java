package ikov.ppestcontrol;

import java.awt.Point;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Game;

public class Login implements Strategy {
	@Override
	public boolean activate() {
		return !Game.isLoggedIn();
	}

	@Override
	public void execute() {
		Mouse.getInstance().click(new Point(380,315));
		Time.sleep(5000);
	}

}
