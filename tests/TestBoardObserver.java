package tests;

import board.BoardObserver;

import java.util.ArrayList;
import java.util.List;

import board.BlockState;

public class TestBoardObserver implements BoardObserver {
	protected List<BlockState> eventHistory;
	
	public TestBoardObserver() {
		eventHistory = new ArrayList<>();
	}
	
	@Override
	public void update(BlockState state) {
		eventHistory.add(state);	
	}
	

}
