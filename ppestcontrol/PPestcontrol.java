package ikov.ppestcontrol;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Timer;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.Loader;
import org.rev317.min.api.events.MessageEvent;
import org.rev317.min.api.events.listeners.MessageListener;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.wrappers.Tile;


@ScriptManifest(author="Agrodon", category = Category.OTHER, description="Pest Control", name="PPestcontrol", servers={"ikov"}, version=0.1)
public class PPestcontrol extends Script implements Paintable, MessageListener{
    private final ArrayList<Strategy> strategies = new ArrayList<Strategy>();
    public static final String[] PORTAL_SIDES = {"West", "SouthWest", "SouthEast","East"};
    public static final int[] PORTAL_IDS = {6142,6145,6144,6143};
    public static final Tile[] PORTAL_TILES = {new Tile(2628,2592), 
    											new Tile(2645,2570),
    											new Tile(2669,2571),
    											new Tile(2680,2590)};
    public static final Tile CENTER_TILE = new Tile(2656, 2595);
    public static final int GANGPLANK_ID = 14315;
    public static final int BRAWLER_ID = 3776;
    
    public static boolean praying = false;
    public static int randomizedPath = 0;
    public static long lastWin = 0;
    public static int gamesWon = 0;
    public static int gamesLost = 0;
    private Timer runTime = new Timer();

    @Override
    public boolean onExecute() {
    	strategies.add(new Login());
    	strategies.add(new Winner());
    	strategies.add(new EnterBoat());
    	strategies.add(new ToPortal());
    	strategies.add(new AttackPortal());
        provide(strategies);
        return true;
    }
    
    @Override
    public void onFinish() {
    }
    
	public void paint(Graphics g) {
		int y = 415;
		Color white = Color.white;
		Color black = Color.black;
	    Graphics2D g2 = (Graphics2D)g;
		int winsHr = (int) ((int)gamesWon*3600000.0D/runTime.getElapsedTime());
		shadowedString(g,"Runtime: "+runTime.toString(), 560, y, white, black);
		//shadowedString(g2,"Runtime: "+hours+":"+minutes+":"+seconds, 560,415, Color.white);
		//shadowedString(g2,"Wins(/hr): "+gamesWon+" ("+winsHr+")", 560, 415, Color.white);
		shadowedString(g2,"Points(/hr): "+gamesWon*10+" ("+winsHr*10+")", 560, y+15, white, black);
		shadowedString(g2,"PPestcontrol",650,463, white, black);
		}
	public void shadowedString(Graphics g, String text, int x, int y, Color front, Color back){
		g.setColor(back);
		g.drawString(text, x+1, y-1);
		g.setColor(front);
		g.drawString(text, x, y);
	}

	public void messageReceived(MessageEvent m) {
		String msg = m.getMessage();
		if(m.getType() == 0){
			if(msg.contains("no points"))
				gamesLost++;
			if(msg.contains("command does not exist") 
					|| msg.contains("already on your")
					|| msg.contains("Unable to receive input")){
				System.out.println(msg);
				System.out.println("Client out of sync? Relogging.");
				forceLogout();
			}
		}
	}
	
	public static boolean inArea(int x, int y, int x2, int y2){
		return Players.getMyPlayer().getLocation().getX() <= x2
				&& Players.getMyPlayer().getLocation().getX() >= x
				&& Players.getMyPlayer().getLocation().getY() <= y2
				&& Players.getMyPlayer().getLocation().getY() >= y;
	}

	//Credits to Minimal for the forceLogout method.
	public static void forceLogout(){
		try {
			Class<?> c = Loader.getClient().getClass();
			Method m = c.getDeclaredMethod("am");
            m.setAccessible(true);
            m.invoke(Loader.getClient());
		}
		catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        	e.printStackTrace();
        }
    }
}
