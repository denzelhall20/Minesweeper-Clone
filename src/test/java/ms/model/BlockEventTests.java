package ms.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ms.events.BlockEvent;

class BlockEventTests {
	
	Block block;
	
	@BeforeEach
	void setup() {
		block = new Block(0, 0, 0);
	}
	
	@Test
	void safeRevealEventTest() {
		block.revealed = true;
		BlockEvent event = new BlockEvent(block, false, false);
		
		assertAll(
				() -> assertTrue(event.isSafeReveal(), "Event is SafeReveal Event."),
				() -> assertFalse(event.isFlag(), "Event is not Flag Event."),
				() -> assertFalse(event.isUnFlag(), "Event is not UnFlag Event."),
				() -> assertFalse(event.isBadFlag(), "Event is not BadFlag Event."),
				() -> assertFalse(event.isBombReveal(), "Event is not BombReveal Event."),
				() -> assertFalse(event.isBombChosen(), "Event is not BombChosen Event.")
		);
	}
	
	@Test
	void bombChosenEventTest() {
		block = new Block(0,0, -1);
		block.revealed = true;
		BlockEvent event = new BlockEvent(block, true, false);
		
		assertAll(
				() -> assertTrue(event.isBombChosen(), "Event is BombChosen Event."),
				() -> assertFalse(event.isFlag(), "Event is not Flag Event."),
				() -> assertFalse(event.isUnFlag(), "Event is not UnFlag Event."),
				() -> assertFalse(event.isBadFlag(), "Event is not BadFlag Event."),
				() -> assertFalse(event.isBombReveal(), "Event is not BombReveal Event."),
				() -> assertFalse(event.isSafeReveal(), "Event is not SafeReveal Event.")
		);
	}
	
	@Test
	void bombRevealEventTest() {
		block = new Block(0,0, -1);
		block.revealed = true;
		BlockEvent event = new BlockEvent(block, false, false);
		
		assertAll(
				() -> assertTrue(event.isBombReveal(), "Event is BombReveal Event."),
				() -> assertFalse(event.isFlag(), "Event is not Flag Event."),
				() -> assertFalse(event.isUnFlag(), "Event is not UnFlag Event."),
				() -> assertFalse(event.isBadFlag(), "Event is not BadFlag Event."),
				() -> assertFalse(event.isSafeReveal(), "Event is not SafeReveal Event."),
				() -> assertFalse(event.isBombChosen(), "Event is not BombChosen Event.")
		);
	}
	
	@Test
	void flagEventTest() {
		block.flagged = true;
		BlockEvent event = new BlockEvent(block, false, false);
		
		assertAll(
				() -> assertTrue(event.isFlag()),
				() -> assertFalse(event.isUnFlag()),
				() -> assertFalse(event.isBadFlag()),
				() -> assertFalse(event.isSafeReveal()),
				() -> assertFalse(event.isSafeReveal()),
				() -> assertFalse(event.isBombReveal()),
				() -> assertFalse(event.isBombChosen())
		);
	}
	
	@Test
	void unFlagEventTest() {
		block.flagged = false;
		BlockEvent event = new BlockEvent(block, false, false);
		
		assertAll(
				() -> assertTrue(event.isUnFlag(), "Event is UnFlag Event."),
				() -> assertFalse(event.isFlag(), "Event is not Flag Event."),
				() -> assertFalse(event.isBadFlag(), "Event is not BadFlag Event."),
				() -> assertFalse(event.isSafeReveal(), "Event is not SafeReveal Event."),
				() -> assertFalse(event.isBombReveal(), "Event is not BombReveal Event."),
				() -> assertFalse(event.isBombChosen(), "Event is not BombChosen Event.")
		);
	}
	
	@Test
	void badFlagEventTest() {
		BlockEvent event = new BlockEvent(block, false, true);
		
		assertAll(
				() -> assertTrue(event.isBadFlag(), "Event is BadFlag Event."),
				() -> assertFalse(event.isFlag(), "Event is not Flag Event."),
				() -> assertFalse(event.isUnFlag(), "Event is not UnFlag Event."),
				() -> assertFalse(event.isSafeReveal(), "Event is not SafeReveal Event."),
				() -> assertFalse(event.isBombReveal(), "Event is not BombReveal Event."),
				() -> assertFalse(event.isBombChosen(), "Event is not BombChosen Event.")
		);
	}
	
	@Test
	void flagBombEventTest() {
		block = new Block(0, 0, -1);
		block.flagged = true;
		BlockEvent event = new BlockEvent(block, false, false);
		
		assertAll(
				() -> assertTrue(event.isFlag(), "Event is Flag Event."),
				() -> assertFalse(event.isBadFlag(), "Event is not BadFlag Event."),
				() -> assertFalse(event.isUnFlag(), "Event is not UnFlag Event."),
				() -> assertFalse(event.isSafeReveal(), "Event is not SafeReveal Event."),
				() -> assertFalse(event.isBombReveal(), "Event is not BombReveal Event."),
				() -> assertFalse(event.isBombChosen(), "Event is not BombChosen Event.")
		);
	}
	
	@Test
	void unFlagBombEventTest() {
		block = new Block(0, 0, -1);
		block.flagged = true;
		block.flagged = false;
		BlockEvent event = new BlockEvent(block, false, false);
		
		assertAll(
				() -> assertTrue(event.isUnFlag(), "Event is unFlag Event."),
				() -> assertFalse(event.isBadFlag(), "Event is not BadFlag Event."),
				() -> assertFalse(event.isFlag(), "Event is not Flag Event."),
				() -> assertFalse(event.isSafeReveal(), "Event is not SafeReveal Event."),
				() -> assertFalse(event.isBombReveal(), "Event is not BombReveal Event."),
				() -> assertFalse(event.isBombChosen(), "Event is not BombChosen Event.")
		);
	}

}
