package halfbytesoftware.game.idle;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    public static class CharacterState {
        // variables
        private String m_name;
        private String m_clazz;
        private int m_graphicId;
        private int m_hpMax;
        private int m_hpCurrent;
        private int m_effectiveStrength;
        private int m_effectiveIntelligence;
        private int m_effectiveDexterity;
        private float m_actionCooldown;

        // methods
        public CharacterState(String name, String clazz, int graphicId){
            this.m_name = name;
            this.m_clazz = clazz;
            this.m_graphicId = graphicId;
            this.m_hpMax = 200;
            this.m_hpCurrent = this.m_hpMax;
            this.m_effectiveStrength = Utils.randomInt(10, 100);
            this.m_effectiveIntelligence = Utils.randomInt(10, 100);
            this.m_effectiveDexterity = Utils.randomInt(0, 100);
        }

        public String getName(){
            return this.m_name;
        }

        public String getClazz(){
            return this.m_clazz;
        }

        public int getGraphicId(){
            return this.m_graphicId;
        }

        public int getHpMax(){
            return this.m_hpMax;
        }

        public int getHpCurrent(){
            return this.m_hpCurrent;
        }

        public void damage(int amount){
            this.m_hpCurrent = Math.max(this.m_hpCurrent - amount, 0);
        }

        public void heal(int amount){
            this.m_hpCurrent = Math.min(this.m_hpCurrent + amount, this.m_hpMax);
        }

        public int getEffectiveStrength(){
            return this.m_effectiveStrength;
        }

        public int getEffectiveIntelligence(){
            return this.m_effectiveIntelligence;
        }

        public int getEffectiveDexterity(){
            return this.m_effectiveDexterity;
        }

        public boolean getIsAlive(){
            return this.m_hpCurrent > 0;
        }

        public float getActionCooldown(){
            return this.m_actionCooldown;
        }

        public void setActionCooldown(float cooldown){
            this.m_actionCooldown = cooldown;
        }

        public void updateActionCooldown(float delta){
            this.m_actionCooldown = Math.max(this.m_actionCooldown - delta, 0);
        }

        public void setHpMax(int amount){
            this.m_hpMax = amount;
        }

        public void resetForBattle(){
            this.m_hpCurrent = this.m_hpMax;
            this.m_actionCooldown = 0.0f;
        }
    }

    public static class CombatAreaInfo{
        // variables
        private String m_levelName;
        private int m_levelLow;
        private int m_levelHigh;
        private boolean m_levelLocked;
        private int m_currentStage;
        private int m_mobsCreatedThisStage;
        private int m_mobsKilledThisStage;

        // methods
        public CombatAreaInfo(String levelName, int levelLow, int levelHigh){
            this.m_levelName = levelName;
            this.m_levelLow = levelLow;
            this.m_levelHigh = levelHigh;
            this.m_levelLocked = true;
            this.m_currentStage = 0;
            this.m_mobsCreatedThisStage = 0;
            this.m_mobsKilledThisStage = 0;
        }

        public String getLevelName(){
            return this.m_levelName;
        }

        public int getLevelLow(){
            return this.m_levelLow;
        }

        public int getLevelHigh(){
            return this.m_levelHigh;
        }

        public boolean getLevelLocked(){
            return this.m_levelLocked;
        }

        public void setLevelLocked(boolean locked){
            this.m_levelLocked = locked;
        }

        public int getCurrentStage(){
            return this.m_currentStage;
        }

        public void setCurrentStage(int stage){
            this.m_currentStage = stage;
        }

        public CharacterState generateMob(){
            // generate a mob state based on low-high level and current stage
            CharacterState cs = new CharacterState("mob", "mob", Utils.randomInt(202, 220));
            cs.setHpMax(this.m_currentStage * 2);
            return cs;
        }

        public int getNumMobsCreatedThisStage(){
            return this.m_mobsCreatedThisStage;
        }

        public void setNumMobsCreatedThisStage(int amount){
            this.m_mobsCreatedThisStage = amount;
        }

        public void modifyNumMobsCreatedThisStage(int delta){
            this.m_mobsCreatedThisStage += delta;
        }

        public int getNumMobsKilledThisStage(){
            return this.m_mobsKilledThisStage;
        }

        public void setNumMobsKilledThisStage(int amount){
            this.m_mobsKilledThisStage = amount;
        }

        public void modifyNumMobsKilledThisStage(int delta){
            this.m_mobsKilledThisStage += delta;
        }

        public void resetForBattle(){
            this.m_mobsCreatedThisStage = 0;
            this.m_mobsKilledThisStage = 0;
        }
    }

    // variables
    private List<CharacterState> m_characterStateList;
    private List<CombatAreaInfo> m_combatAreaInfoList;

    // methods
    public GameState(){
        // characters
        this.m_characterStateList = new ArrayList<>();
        this.m_characterStateList.add(new CharacterState("war", "warrior", 22));
        this.m_characterStateList.add(new CharacterState("mag", "mage", 23));
        this.m_characterStateList.add(new CharacterState("pri", "priest", 24));

        // combat area
        this.m_combatAreaInfoList = new ArrayList<>();
        for (int i = 0; i < 25; ++i){
            this.m_combatAreaInfoList.add(new CombatAreaInfo("Area " + (i + 1), i * 10 + 1, i * 10 + 10));
        }
        this.m_combatAreaInfoList.get(0).setLevelLocked(false);
    }

    public List<CharacterState> getCharacterStateList(){
        return this.m_characterStateList;
    }

    public List<CombatAreaInfo> getCombatAreaInfoList(){
        return this.m_combatAreaInfoList;
    }
}
