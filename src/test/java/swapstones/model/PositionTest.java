package swapstones.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionTest {

    Position position;

    void assertPosition(int expectedCol, Stone expectedStone, Position position) {
        assertAll("position",
                () -> assertEquals(expectedCol, position.col()),
                () -> assertEquals(expectedStone, position.stone())
        );
    }

    @BeforeEach
    void init() {
        position = new Position(0, Stone.HEAD);
    }

    @Test
    void testPositionInitialization() {
        assertPosition(0, Stone.HEAD, position);
    }

    @Test
    void testPositionValues() {
        Position newPosition = new Position(1, Stone.TAIL);
        assertPosition(1, Stone.TAIL, newPosition);
    }

    @Test
    void testSetCol() {
        Position newPosition = new Position(1, Stone.HEAD);
        assertEquals(1, newPosition.col());
    }

    @Test
    void testSetStone() {
        Position newPosition = new Position(1, Stone.TAIL);
        assertEquals(Stone.TAIL, newPosition.stone());
    }

    @Test
    void testToString() {
        assertEquals("Position{col=0, stone=HEAD}", position.toString());
    }
}
