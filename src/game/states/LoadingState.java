package game.states;

import game.Globals;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/**
 * Loading state where all the resources of the game are loaded. While loading,
 * the user is presented a progress bar with a percentage. In the lower left
 * corner the currently loading resource is shown.
 * 
 * @author 
 */
public class LoadingState extends BasicGameState {

        private DeferredResource nextResource = null;

        private int barWidth = Globals.APP_WIDTH / 3;
        private int yPos = Globals.APP_HEIGHT / 2 - 50;

        private Rectangle bar = null;
        private Rectangle fill = null;

 

		@Override
        public int getID() {
                return 5; 
        }

        @Override
        public void init(GameContainer container, StateBasedGame game) throws SlickException {
//                font = FontFactory.createFont(ResourceManager.getFont("visitor").getFontFile(), java.awt.Color.WHITE);

                bar = new Rectangle(Globals.APP_WIDTH / 2 - barWidth / 2, yPos, barWidth, 20);
                fill = new Rectangle(Globals.APP_WIDTH / 2 - barWidth / 2, yPos, 0, 20);
        }

        @Override
        public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

                if (nextResource != null) {
                        g.setColor(Color.gray);
                        g.drawString(nextResource.getDescription(), 10, Globals.APP_HEIGHT - 20);
                }

                double total = LoadingList.get().getTotalResources();
                double loaded = LoadingList.get().getTotalResources() - LoadingList.get().getRemainingResources();
                double amount = loaded / total;

                fill.setWidth((float) (bar.getWidth() * amount));

                g.setColor(Color.white);
                g.drawString("Loading ...", Globals.APP_WIDTH / 2 - barWidth / 2, yPos - 20);
                g.fill(fill);
                g.draw(bar);

                g.setColor(Color.red);
                String percentStr = Math.round(amount * 100) + "%";
                g.drawString(percentStr, bar.getCenterX() , bar.getCenterY() - 8);

                g.setColor(Color.white);
        }

        @Override
        public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
                if (nextResource != null) {
                        try {
                                nextResource.load();
                        } catch (Exception e) {
                                throw new SlickException("Failed to load: " + nextResource.getDescription(), e);
                        }

                        nextResource = null;
                }

                if (LoadingList.get().getRemainingResources() > 0) {
                        nextResource = LoadingList.get().getNext();
                } else {
                        game.enterState(Globals.PLAY);
                }
        }

}