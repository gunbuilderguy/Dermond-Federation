package data.scripts.shipsystems;


import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
import com.fs.starfarer.api.util.IntervalUtil;
import data.scripts.util.MagicRender;
import org.dark.graphics.util.AnamorphicFlare;
import org.dark.shaders.distortion.DistortionShader;
import org.dark.shaders.distortion.RippleDistortion;
import org.lazywizard.lazylib.FastTrig;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import org.dark.shaders.distortion.WaveDistortion;
import org.dark.shaders.light.LightShader;
import org.dark.shaders.light.StandardLight;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.CombatUtils;

public class dermond_orlando extends BaseShipSystemScript {

    public static final Color JITTER_COLOR = new Color(56, 146, 245,55);

    public static final Color JITTER_UNDER_COLOR = new Color(129, 178, 245,155);

    private final Color AFTER_IMAGE_COLOR = new Color(19, 126, 97, 191);

    private List<ShipAPI> have_phased = new ArrayList<>();

    private final IntervalUtil interval = new IntervalUtil(0.3F, 0.3F);

    public static boolean shit = false;

    //private static final String CHARGEUP_SOUND = "system_deracinatorcharge";
    private static final float DAMAGE_MOD_VS_CAPITAL = 0.15f;
    private static final float DAMAGE_MOD_VS_CRUISER = 0.20f;
    private static final float DAMAGE_MOD_VS_DESTROYER = 0.5f;
    private static final float DAMAGE_MOD_VS_FIGHTER = 0.7f;
    private static final float DAMAGE_MOD_VS_FRIGATE = 0.8f;

    //Distortion constants
    private static final float DISTORTION_BLAST_RADIUS = 1200f;
    private static final float DISTORTION_CHARGE_RADIUS = 100f;

    // Explosion effect constants
    private static final Color EXPLOSION_COLOR = new Color(55, 160, 88);
    private static final float EXPLOSION_DAMAGE_AMOUNT = 1350f;
    private static final DamageType EXPLOSION_DAMAGE_TYPE = DamageType.ENERGY;
    private static final float EXPLOSION_DAMAGE_VS_ALLIES_MODIFIER = .11f;
    private static final float EXPLOSION_EMP_DAMAGE_AMOUNT = 2000f;
    private static final float EXPLOSION_EMP_VS_ALLIES_MODIFIER = .05f;
    private static final float EXPLOSION_FORCE_VS_ALLIES_MODIFIER = .3f;
    private static final float EXPLOSION_PUSH_RADIUS = 800f;
    //private static final String EXPLOSION_SOUND = "luciferdriveactivate";
    private static final float EXPLOSION_VISUAL_RADIUS = 1250f;
    private static final Color FLARE_COLOR = new Color(55, 242, 221);
    private static final float FORCE_VS_ASTEROID = 290f;
    private static final float FORCE_VS_CAPITAL = 35f;
    private static final float FORCE_VS_CRUISER = 60f;
    private static final float FORCE_VS_DESTROYER = 115f;
    private static final float FORCE_VS_FIGHTER = 255f;
    private static final float FORCE_VS_FRIGATE = 180f;

    private static final int MAX_PARTICLES_PER_FRAME = 30; // Based on charge level

    // "Inhale" effect constants
    private static final Color PARTICLE_COLOR = new Color(155, 240, 200);
    private static final float PARTICLE_OPACITY = 0.85f;
    private static final float PARTICLE_RADIUS = 300f;
    private static final float PARTICLE_SIZE = 6f;

    private static final Vector2f ZERO = new Vector2f();

    //Local variables, don't touch these
    private boolean isActive = false;
    private StandardLight light;
    private WaveDistortion wave;

    private IntervalUtil interval_for_emp = new IntervalUtil(0, 0);

    private List<EmpArcEntityAPI> Emp_arc_list = new ArrayList();

    private Vector2f interval_size_emp = new Vector2f(0.1f,  0.5f);

    private boolean spawnCircle = false;

    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        CombatEngineAPI engine = Global.getCombatEngine();
        ShipAPI ship = null;
        boolean player = false;
        if (stats.getEntity() instanceof ShipAPI) {
            ship = (ShipAPI) stats.getEntity();
            player = ship == Global.getCombatEngine().getPlayerShip();
            id = id + "_" + ship.getId();
        } else {
            return;
        }

        if (Global.getCombatEngine().isPaused() || state == State.IDLE) {
            return;
        }

        if (state == State.OUT) {
            unapply(stats, id);
            ship.getSystem().setCooldownRemaining(2f);
        }

        //Ripple
        //this.createRipple((ShipAPI)stats.getEntity(), 20f);

        //Taken from SOTF, but edited by me so it suits the idea
        //Anyways I am Pirate, and we believe in Freedom on The Internet

        float level = effectLevel;
        float levelForAlpha = level;

        //when you are charging and have no yet phased
        if (state == State.IN && !have_phased.contains(ship) && Global.getCombatEngine().getTotalElapsedTime(false) > 1f) {
            Vector2f loc = new Vector2f(ship.getLocation());
            loc.x -= 70f * FastTrig.cos(ship.getFacing() * Math.PI / 180f);
            loc.y -= 70f * FastTrig.sin(ship.getFacing() * Math.PI / 180f);

            stats.getArmorDamageTakenMult().modifyMult(id, 0.05f);
            stats.getEmpDamageTakenMult().modifyMult(id, 0f);
            stats.getEngineDamageTakenMult().modifyMult(id, 0.2f);


            //For EMP lightning Effect
            float amount = Global.getCombatEngine().getElapsedInLastFrame();

            interval_for_emp.advance(amount);

            if (interval_for_emp.intervalElapsed() && !have_phased.contains(ship) && state == State.IN) {

                interval_for_emp = new IntervalUtil(interval_size_emp.x, interval_size_emp.y);

                Vector2f start = ship.getLocation();
                float radius_of_ship = MathUtils.getRandomNumberInRange(180, ship.getShield().getRadius());
                Vector2f end = MathUtils.getRandomPointOnCircumference(start, radius_of_ship);

                EmpArcEntityAPI something = Global.getCombatEngine().spawnEmpArcVisual(start,ship,end,null,10f,FLARE_COLOR,JITTER_UNDER_COLOR);
                Emp_arc_list.add(something);


            }



            //This runs only once
            if (!isActive) {
                isActive = true;
                //Global.getSoundPlayer().playSound(CHARGEUP_SOUND, 1f, 1f, ship.getLocation(), ship.getVelocity());

                light = new StandardLight(loc, ZERO, ZERO, null);
                light.setIntensity(1.25f);
                light.setSize(EXPLOSION_VISUAL_RADIUS);
                light.setColor(PARTICLE_COLOR);
                light.fadeIn(1.95f);
                light.setLifetime(3f);
                light.setAutoFadeOutTime(0.17f);
                LightShader.addLight(light);
                /*
                if (shit == false && Global.getCombatEngine().getTotalElapsedTime(false) > 2f) {
                    shit = true;
                    wave = new WaveDistortion(loc, ZERO);
                    wave.setSize(DISTORTION_CHARGE_RADIUS);
                    wave.setIntensity(DISTORTION_CHARGE_RADIUS / 4f);
                    wave.fadeInSize(1.95f);
                    wave.fadeInIntensity(1.95f);
                    wave.setLifetime(0.05f);
                    wave.setAutoFadeSizeTime(-0.5f);
                    wave.setAutoFadeIntensityTime(0.17f);
                    DistortionShader.addDistortion(wave);
                } else {
                    wave.setLocation(loc);
                }*/
            } else {
                light.setLocation(loc);
            }

        }


        if(ship.getSystem().getState()!= ShipSystemAPI.SystemState.IN && !spawnCircle){
            Vector2f loc = new Vector2f(ship.getLocation());
            loc.x -= 70f * FastTrig.cos(ship.getFacing() * Math.PI / 180f);
            loc.y -= 70f * FastTrig.sin(ship.getFacing() * Math.PI / 180f);
            spawnCircle=true;
            MagicRender.battlespace(
                    Global.getSettings().getSprite("fx","circle"),
                    ship.getLocation(),
                    new Vector2f(0f,0f),
                    new Vector2f(0f,0f),
                    new Vector2f(500f, 500f),
                    0f,
                    3f,
                    Color.WHITE,
                    0f,
                    0f,
                    0f,
                    0f,
                    2f,
                    0.5f,
                    0.000001f,
                    0.2f,
                    CombatEngineLayers.JUST_BELOW_WIDGETS,
                    GL11.GL_ONE_MINUS_DST_COLOR,
                    GL11.GL_ONE_MINUS_SRC_ALPHA
            );
            //Creates Flares
            Vector2f particlePos, particleVel;
            int numParticlesThisFrame = Math.round(effectLevel * MAX_PARTICLES_PER_FRAME);
            for (int x = 0; x < numParticlesThisFrame; x++) {
                particlePos = MathUtils.getRandomPointOnCircumference(ship.getLocation(), PARTICLE_RADIUS);
                particleVel = Vector2f.sub(ship.getLocation(), particlePos, null);
                Global.getCombatEngine().addSmokeParticle(particlePos, particleVel, PARTICLE_SIZE, PARTICLE_OPACITY, 1f,
                        PARTICLE_COLOR);
            }


            //The explosion itself
            ShipAPI victim;
            Vector2f dir;
            float force, damage, emp, mod;
            List<CombatEntityAPI> entities = CombatUtils.getEntitiesWithinRange(ship.getLocation(),
                    EXPLOSION_PUSH_RADIUS);
            int size = entities.size();
            for (int i = 0; i < size; i++) {
                CombatEntityAPI tmp = entities.get(i);
                if (tmp == ship) {
                    continue;
                }

                mod = 1f - (MathUtils.getDistance(ship, tmp) / EXPLOSION_PUSH_RADIUS);
                force = FORCE_VS_ASTEROID * mod;
                damage = EXPLOSION_DAMAGE_AMOUNT * mod;
                emp = EXPLOSION_EMP_DAMAGE_AMOUNT * mod;

                if (tmp instanceof ShipAPI) {
                    victim = (ShipAPI) tmp;

                    // Modify push strength based on ship class
                    if (victim.getHullSize() == ShipAPI.HullSize.FIGHTER) {
                        force = FORCE_VS_FIGHTER * mod;
                        damage /= DAMAGE_MOD_VS_FIGHTER;
                    } else if (victim.getHullSize() == ShipAPI.HullSize.FRIGATE) {
                        force = FORCE_VS_FRIGATE * mod;
                        damage /= DAMAGE_MOD_VS_FRIGATE;
                    } else if (victim.getHullSize() == ShipAPI.HullSize.DESTROYER) {
                        force = FORCE_VS_DESTROYER * mod;
                        damage /= DAMAGE_MOD_VS_DESTROYER;
                    } else if (victim.getHullSize() == ShipAPI.HullSize.CRUISER) {
                        force = FORCE_VS_CRUISER * mod;
                        damage /= DAMAGE_MOD_VS_CRUISER;
                    } else if (victim.getHullSize() == ShipAPI.HullSize.CAPITAL_SHIP) {
                        force = FORCE_VS_CAPITAL * mod;
                        damage /= DAMAGE_MOD_VS_CAPITAL;
                    }

                    if (victim.getOwner() == ship.getOwner()) {
                        damage *= EXPLOSION_DAMAGE_VS_ALLIES_MODIFIER;
                        emp *= EXPLOSION_EMP_VS_ALLIES_MODIFIER;
                        force *= EXPLOSION_FORCE_VS_ALLIES_MODIFIER;
                    }



                    if ((victim.getShield() != null && victim.getShield().isOn() && victim.getShield().isWithinArc(ship.getLocation()))) {
                        victim.getFluxTracker().increaseFlux(damage * 2, true);
                    } else {
                        ShipAPI empTarget = victim;
                        for (int x = 0; x < 5; x++) {
                            engine.spawnEmpArc(ship, MathUtils.getRandomPointInCircle(victim.getLocation(),
                                            victim.getCollisionRadius()),
                                    empTarget,
                                    empTarget, EXPLOSION_DAMAGE_TYPE, damage / 10, emp / 5,
                                    EXPLOSION_PUSH_RADIUS, null, 2f, EXPLOSION_COLOR,
                                    EXPLOSION_COLOR);
                        }
                    }
                }

                dir = VectorUtils.getDirectionalVector(ship.getLocation(), tmp.getLocation());
                dir.scale(force);

                Vector2f.add(tmp.getVelocity(), dir, tmp.getVelocity());
            }



        }


        if(state != State.IN){
            for(EmpArcEntityAPI E:new ArrayList<EmpArcEntityAPI>(Emp_arc_list)){
                Global.getCombatEngine().removeEntity(E);
                Emp_arc_list.remove(E);
            }
        }


        //After the system ends buildup and activates for real

        if (state == State.ACTIVE) {


            //Base effects
            ship.setPhased(true);
            levelForAlpha = level;
            stats.getMaxSpeed().modifyFlat(id, 105f * effectLevel);
            stats.getAcceleration().modifyFlat(id, 600f * effectLevel);
            stats.getTurnAcceleration().modifyFlat(id, 250 * effectLevel);

            stats.getArmorDamageTakenMult().unmodifyMult(id);
            stats.getEmpDamageTakenMult().unmodifyMult(id);
            stats.getEngineDamageTakenMult().unmodifyMult(id);



            Vector2f loc = new Vector2f(ship.getLocation());
            loc.x -= 70f * FastTrig.cos(ship.getFacing() * Math.PI / 180f);
            loc.y -= 70f * FastTrig.sin(ship.getFacing() * Math.PI / 180f);


            //Explosion, currently not used
            /*
            if (!isActive) {
                isActive = true;
                //Global.getSoundPlayer().playSound(CHARGEUP_SOUND, 1f, 1f, ship.getLocation(), ship.getVelocity());

                light = new StandardLight(loc, ZERO, ZERO, null);
                light.setIntensity(1.25f);
                light.setSize(EXPLOSION_VISUAL_RADIUS);
                light.setColor(PARTICLE_COLOR);
                light.fadeIn(1.95f);
                light.setLifetime(0.1f);
                light.setAutoFadeOutTime(0.17f);
                LightShader.addLight(light);

                wave = new WaveDistortion(loc, ZERO);
                wave.setSize(DISTORTION_CHARGE_RADIUS);
                wave.setIntensity(DISTORTION_CHARGE_RADIUS / 4f);
                wave.fadeInSize(1.95f);
                wave.fadeInIntensity(1.95f);
                wave.setLifetime(0.1f);
                wave.setAutoFadeSizeTime(-0.5f);
                wave.setAutoFadeIntensityTime(0.17f);
                DistortionShader.addDistortion(wave);
            } else {
                light.setLocation(loc);
                wave.setLocation(loc);
            }*/

            //Creates Flares
            Vector2f particlePos, particleVel;
            int numParticlesThisFrame = Math.round(effectLevel * MAX_PARTICLES_PER_FRAME);
            for (int x = 0; x < numParticlesThisFrame; x++) {
                particlePos = MathUtils.getRandomPointOnCircumference(ship.getLocation(), PARTICLE_RADIUS);
                particleVel = Vector2f.sub(ship.getLocation(), particlePos, null);
                Global.getCombatEngine().addSmokeParticle(particlePos, particleVel, PARTICLE_SIZE, PARTICLE_OPACITY, 1f,
                        PARTICLE_COLOR);
            }


            //Jitter effect
            float jitterLevel = effectLevel;
            float jitterRangeBonus = 0;
            float maxRangeBonus = 10f;
            if (state == State.IN) {
                jitterLevel = effectLevel / (1f / ship.getSystem().getChargeUpDur());
                if (jitterLevel > 1) {
                    jitterLevel = 1f;
                }
                jitterRangeBonus = jitterLevel * maxRangeBonus;
            } else if (state == State.ACTIVE) {
                jitterLevel = 1f;
                jitterRangeBonus = maxRangeBonus;
            } else if (state == State.OUT) {
                jitterRangeBonus = jitterLevel * maxRangeBonus;
            }
            jitterLevel = (float) Math.sqrt(jitterLevel);
            effectLevel *= effectLevel;

            ship.setJitter(this, JITTER_COLOR, jitterLevel, 3, 0, 0 + jitterRangeBonus);
            ship.setJitterUnder(this, JITTER_UNDER_COLOR, jitterLevel, 25, 0f, 7f + jitterRangeBonus);


            //The effect where the ships are left after you move
            SpriteAPI sprite = ship.getSpriteAPI();
            float offsetX = sprite.getWidth() / 2.0F - sprite.getCenterX();
            float offsetY = sprite.getHeight() / 2.0F - sprite.getCenterY();
            this.interval.advance(engine.getElapsedInLastFrame());
            if (this.interval.intervalElapsed()) {
                offsetX = (float) FastTrig.cos(Math.toRadians((double)(ship.getFacing() - 90.0F))) * offsetX - (float)FastTrig.sin(Math.toRadians((double)(ship.getFacing() - 90.0F))) * offsetY;
                offsetY = (float)FastTrig.sin(Math.toRadians((double)(ship.getFacing() - 90.0F))) * offsetX + (float)FastTrig.cos(Math.toRadians((double)(ship.getFacing() - 90.0F))) * offsetY;
                MagicRender.battlespace(Global.getSettings().getSprite(ship.getHullSpec().getSpriteName()), new Vector2f(ship.getLocation().getX() + offsetX, ship.getLocation().getY() + offsetY), new Vector2f(0.0F, 0.0F), new Vector2f(ship.getSpriteAPI().getWidth(), ship.getSpriteAPI().getHeight()), new Vector2f(0.0F, 0.0F), ship.getFacing() - 90.0F, 0.0F, this.AFTER_IMAGE_COLOR, true, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1F, 0.1F, 1.0F, CombatEngineLayers.BELOW_SHIPS_LAYER);
            }



            //For the phased in
            levelForAlpha = level;
            ship.setExtraAlphaMult(1f - (0.9f) * levelForAlpha);
            float shipTimeMult = 50f + (2f) * levelForAlpha;
            stats.getTimeMult().modifyMult(id, shipTimeMult);
            if (player) {
                Global.getCombatEngine().getTimeMult().modifyMult(id, 1f / shipTimeMult);
            } else {
                Global.getCombatEngine().getTimeMult().unmodify(id);
            }




        } else if (state == State.OUT) { //When phasing out
            stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
            stats.getTimeMult().unmodifyMult(id);
            ship.setPhased(false);


            // After it has ended

            // Everything in this section is only done once per cooldown
            if (isActive) {
                engine.spawnExplosion(ship.getLocation(), ship.getVelocity(), EXPLOSION_COLOR, EXPLOSION_VISUAL_RADIUS,
                        0.2f);
                engine.spawnExplosion(ship.getLocation(), ship.getVelocity(), EXPLOSION_COLOR, EXPLOSION_VISUAL_RADIUS /
                        2f, 0.2f);

                Vector2f loc = new Vector2f(ship.getLocation());
                loc.x -= 70f * FastTrig.cos(ship.getFacing() * Math.PI / 180f);
                loc.y -= 70f * FastTrig.sin(ship.getFacing() * Math.PI / 180f);
                //Creates jsut light after the explosion, so it's not there just for couple of frames
                light = new StandardLight();
                light.setLocation(loc);
                light.setIntensity(2f);
                light.setSize(EXPLOSION_VISUAL_RADIUS * 2f);
                light.setColor(EXPLOSION_COLOR);
                light.fadeOut(1.25f);
                LightShader.addLight(light);

                //Creates a small wave distortion after the explosion
                wave = new WaveDistortion();
                wave.setLocation(loc);
                wave.setSize(DISTORTION_BLAST_RADIUS);
                wave.setIntensity(DISTORTION_BLAST_RADIUS * 0.075f);
                wave.fadeInSize(1.2f);
                wave.fadeOutIntensity(0.9f);
                wave.setSize(DISTORTION_BLAST_RADIUS * 0.25f);
                DistortionShader.addDistortion(wave);

                //Global.getSoundPlayer().playSound(EXPLOSION_SOUND, 1f, 1f, ship.getLocation(), ship.getVelocity());


                //Creates  a small green explosion
                AnamorphicFlare.createFlare(ship, new Vector2f(loc), engine, 0.50f, 0.05f, -15f + (float) Math.random() * 30f, 9.25f, 6f, FLARE_COLOR, PARTICLE_COLOR);
                AnamorphicFlare.createFlare(ship, new Vector2f(loc), engine, 0.51f, 0.049f, -15f + (float) Math.random() * 60f, 8.95f, 6f, PARTICLE_COLOR, FLARE_COLOR);
                AnamorphicFlare.createFlare(ship, new Vector2f(loc), engine, 0.52f, 0.048f, -15f + (float) Math.random() * 30f, 7.55f, 6f, FLARE_COLOR, PARTICLE_COLOR);
                AnamorphicFlare.createFlare(ship, new Vector2f(loc), engine, 0.51f, 0.047f, -15f + (float) Math.random() * 90f, 10.95f, 6f, PARTICLE_COLOR, FLARE_COLOR);
                AnamorphicFlare.createFlare(ship, new Vector2f(loc), engine, 0.50f, 0.046f, -15f + (float) Math.random() * 120f, 8.55f, 6f, FLARE_COLOR, PARTICLE_COLOR);



                //The explosion itself
                ShipAPI victim;
                Vector2f dir;
                float force, damage, emp, mod;
                List<CombatEntityAPI> entities = CombatUtils.getEntitiesWithinRange(ship.getLocation(),
                        EXPLOSION_PUSH_RADIUS);
                int size = entities.size();
                for (int i = 0; i < size; i++) {
                    CombatEntityAPI tmp = entities.get(i);
                    if (tmp == ship) {
                        continue;
                    }

                    mod = 1f - (MathUtils.getDistance(ship, tmp) / EXPLOSION_PUSH_RADIUS);
                    force = FORCE_VS_ASTEROID * mod;
                    damage = EXPLOSION_DAMAGE_AMOUNT * mod;
                    emp = EXPLOSION_EMP_DAMAGE_AMOUNT * mod;

                    if (tmp instanceof ShipAPI) {
                        victim = (ShipAPI) tmp;

                        // Modify push strength based on ship class
                        if (victim.getHullSize() == ShipAPI.HullSize.FIGHTER) {
                            force = FORCE_VS_FIGHTER * mod;
                            damage /= DAMAGE_MOD_VS_FIGHTER;
                        } else if (victim.getHullSize() == ShipAPI.HullSize.FRIGATE) {
                            force = FORCE_VS_FRIGATE * mod;
                            damage /= DAMAGE_MOD_VS_FRIGATE;
                        } else if (victim.getHullSize() == ShipAPI.HullSize.DESTROYER) {
                            force = FORCE_VS_DESTROYER * mod;
                            damage /= DAMAGE_MOD_VS_DESTROYER;
                        } else if (victim.getHullSize() == ShipAPI.HullSize.CRUISER) {
                            force = FORCE_VS_CRUISER * mod;
                            damage /= DAMAGE_MOD_VS_CRUISER;
                        } else if (victim.getHullSize() == ShipAPI.HullSize.CAPITAL_SHIP) {
                            force = FORCE_VS_CAPITAL * mod;
                            damage /= DAMAGE_MOD_VS_CAPITAL;
                        }

                        if (victim.getOwner() == ship.getOwner()) {
                            damage *= EXPLOSION_DAMAGE_VS_ALLIES_MODIFIER;
                            emp *= EXPLOSION_EMP_VS_ALLIES_MODIFIER;
                            force *= EXPLOSION_FORCE_VS_ALLIES_MODIFIER;
                        }



                        if ((victim.getShield() != null && victim.getShield().isOn() && victim.getShield().isWithinArc(ship.getLocation()))) {
                            victim.getFluxTracker().increaseFlux(damage * 2, true);
                        } else {
                            ShipAPI empTarget = victim;
                            for (int x = 0; x < 5; x++) {
                                engine.spawnEmpArc(ship, MathUtils.getRandomPointInCircle(victim.getLocation(),
                                                victim.getCollisionRadius()),
                                        empTarget,
                                        empTarget, EXPLOSION_DAMAGE_TYPE, damage / 10, emp / 5,
                                        EXPLOSION_PUSH_RADIUS, null, 2f, EXPLOSION_COLOR,
                                        EXPLOSION_COLOR);
                            }
                        }
                    }

                    dir = VectorUtils.getDirectionalVector(ship.getLocation(), tmp.getLocation());
                    dir.scale(force);

                    Vector2f.add(tmp.getVelocity(), dir, tmp.getVelocity());
                }

                isActive = false;
            }
        }


    }

    public void unapply(MutableShipStatsAPI stats, String id) {

        ShipAPI ship = null;
        boolean player = false;
        if (stats.getEntity() instanceof ShipAPI) {
            ship = (ShipAPI) stats.getEntity();
            player = ship == Global.getCombatEngine().getPlayerShip();
            id = id + "_" + ship.getId();
        } else {
            return;
        }

        Global.getCombatEngine().getTimeMult().unmodify(id);
        stats.getTimeMult().unmodify(id);
        stats.getMaxSpeed().unmodify(id);
        stats.getMaxTurnRate().unmodify(id);
        stats.getTurnAcceleration().unmodify(id);
        stats.getAcceleration().unmodify(id);
        stats.getDeceleration().unmodify(id);

        isActive = false;

        if(ship.getSystem().getState()== ShipSystemAPI.SystemState.COOLDOWN && spawnCircle){
            spawnCircle=false;
        }

        ship.setPhased(false);
        ship.setExtraAlphaMult(1f);

    }


    public StatusData getStatusData(int index, State state, float effectLevel) {
        if (index == 0) {
            return new StatusData("ERROR", false);
        }
        return null;
    }




}
