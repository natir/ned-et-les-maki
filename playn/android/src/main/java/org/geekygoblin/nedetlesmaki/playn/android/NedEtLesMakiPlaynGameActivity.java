package org.geekygoblin.nedetlesmaki.playn.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import org.geekygoblin.nedetlesmaki.playn.core.NedEtLesMakiPlaynGame;

public class NedEtLesMakiPlaynGameActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new NedEtLesMakiPlaynGame());
  }
}
