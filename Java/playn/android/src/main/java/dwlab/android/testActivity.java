package dwlab.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import dwlab.core.test;

public class testActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new test());
  }
}
