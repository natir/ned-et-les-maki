package org.geekygoblin.nedetlesmaki.playn.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import org.geekygoblin.nedetlesmaki.playn.core.NedEtLesMakiPlaynGame;

public class NedEtLesMakiPlaynGameHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    config.mode = HtmlPlatform.Mode.CANVAS;
    HtmlPlatform platform = HtmlPlatform.register(config);
    platform.assets().setPathPrefix("playn/");
    PlayN.run(new NedEtLesMakiPlaynGame());
  }
}
