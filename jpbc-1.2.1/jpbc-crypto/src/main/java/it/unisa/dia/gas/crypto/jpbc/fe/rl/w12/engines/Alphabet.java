package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class Alphabet {
    private int size;
    private Map<Character, Integer> map;

    public Alphabet() {
        this.size = 0;
        this.map = new HashMap<Character, Integer>();
    }

    public void addLetter(Character... characters) {
        for (Character character : characters) {
            map.put(character, size++);
        }
    }

    public int getSize() {
        return size;
    }

    public int getIndex(Character character) {
        return map.get(character);
    }
}
