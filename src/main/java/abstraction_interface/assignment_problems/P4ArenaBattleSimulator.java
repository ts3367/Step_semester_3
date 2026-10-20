package abstraction_interface.assignment_problems;

// P4. Arena Battle Simulator
// Warrior IS-A GameCharacter and CAN-DO Attackable + Defendable;
// Trap is only Defendable and was never meant to be a character.
public class P4ArenaBattleSimulator {

    // Two overloads -> compile-time polymorphism.
    public interface Attackable {
        String attack();
        String attack(String weaponName);
    }

    public interface Defendable {
        String defend();
    }

    // Abstract class: only real characters live here.
    public abstract static class GameCharacter {
        // Shared static counter: incremented once inside GameCharacter's own constructor.
        private static int characterCounter = 1000;

        private final String characterId;

        protected GameCharacter() {
            characterCounter++;
            this.characterId = "CHR-" + characterCounter;
        }

        public abstract String getSpecialMove();

        public String getCharacterId() {
            return characterId;
        }
    }

    public static class Warrior extends GameCharacter implements Attackable, Defendable {
        private final String name;

        public Warrior(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String attack() {
            return name + " strikes with a blade";
        }

        @Override
        public String attack(String weaponName) {
            return name + " strikes with an " + weaponName;
        }

        @Override
        public String defend() {
            return name + " raises a shield";
        }

        @Override
        public String getSpecialMove() {
            return name + " unleashes Whirlwind Slash";
        }
    }

    // Only Defendable: no characterId, no getSpecialMove() - it was never a character.
    public static class Trap implements Defendable {
        private final String trapType;

        public Trap(String trapType) {
            this.trapType = trapType;
        }

        public String getTrapType() {
            return trapType;
        }

        @Override
        public String defend() {
            return trapType + " triggers automatically";
        }
    }

    // Loops a mixed array of Warriors and Traps and calls defend() on each.
    public static void resolveDefense(Defendable[] combatants) {
        if (combatants == null) return;
        for (Defendable combatant : combatants) {
            if (combatant == null) continue;
            System.out.println(combatant.defend());
        }
    }

    public static void main(String[] args) {
        Warrior w = new Warrior("Kael");
        System.out.println(w.attack());               // Kael strikes with a blade
        System.out.println(w.attack("Iron Sword"));  // Kael strikes with an Iron Sword
        System.out.println(w.defend());               // Kael raises a shield
        System.out.println(w.getSpecialMove());      // Kael unleashes Whirlwind Slash
        System.out.println(w.getCharacterId());       // CHR-1001

        Trap t = new Trap("Spike Pit");
        System.out.println(t.defend());               // Spike Pit triggers automatically

        resolveDefense(new Defendable[]{w, t});
    }
}
