package org.geekygoblin.nedetlesmaki.playn.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import org.geekygoblin.nedetlesmaki.playn.core.NedEtLesMakiPlaynGame;

public class NedEtLesMakiPlaynGameHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    // use config to customize the HTML platform, if needed
    HtmlPlatform platform = HtmlPlatform.register(config);
    platform.assets().setPathPrefix("nedetlesmaki-playn/");
    PlayN.run(new NedEtLesMakiPlaynGame());
  }
}
