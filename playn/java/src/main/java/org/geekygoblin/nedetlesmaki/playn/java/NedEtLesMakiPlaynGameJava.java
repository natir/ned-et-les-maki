package org.geekygoblin.nedetlesmaki.playn.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import org.geekygoblin.nedetlesmaki.playn.core.NedEtLesMakiPlaynGame;

public class NedEtLesMakiPlaynGameJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    config.appName = "Ned et les maki";
    JavaPlatform.register(config);
    PlayN.run(new NedEtLesMakiPlaynGame());
  }
}
