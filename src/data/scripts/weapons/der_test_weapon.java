package data.scripts.weapons;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

public class der_test_weapon implements EveryFrameScript {




    public void spritepos(WeaponAPI base, WeaponAPI movedweapon, float offsetX, float offsetY){

        if(base != null && movedweapon != null){
            Vector2f wloc = VectorUtils.rotateAroundPivot(new Vector2f(base.getSlot().getLocation().getX() + offsetX, base.getSlot().getLocation().getY() + offsetY), base.getSlot().getLocation(), base.getCurrAngle() - base.getShip().getFacing() - 90f);
            movedweapon.getSprite().setCenter(wloc.getX()+movedweapon.getSprite().getHeight()/2f, wloc.getY()+movedweapon.getSprite().getWidth()/2f);
        }

    }


    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {

    }
}
