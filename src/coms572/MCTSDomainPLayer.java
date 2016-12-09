package coms572;

public interface MCTSDomainPLayer<Board extends MCTSDomainBoard> {
	Board performSimulationFromState(Board b);
    double getValueFromTState(Board terminalState);
}
