import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JLabel;

public class MonitoringLabel extends JLabel {

   public MonitoringLabel(Icon image) {
      super(image);
   }

   public void addActionListener(ActionListener listener) {
      this.listenerList.add(ActionListener.class, listener);
   }

   protected void fireActionPerformed() {
      ActionListener[] listeners = (ActionListener[])this.listenerList.getListeners(ActionListener.class);
      if (listeners.length > 0) {
         ActionEvent evt = new ActionEvent(this, 0, "stopped");
         ActionListener[] var3 = listeners;
         int var4 = listeners.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ActionListener listener = var3[var5];
            listener.actionPerformed(evt);
         }
      }
   }

   public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
      boolean finished = super.imageUpdate(img, infoflags, x, y, w, h);
      if (!finished) {
         this.fireActionPerformed();
      }

      return finished;
   }
}