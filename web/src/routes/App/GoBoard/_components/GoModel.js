import GoError from './GoError';
import uuidV4 from 'uuid/v4';

const WHITE = 'WHITE';
const BLACK = 'BLACK';

class GoModel {
    constructor() {
        this.states = {};
        let initialState = new State();
        this.states[initialState.id] = initialState;
        this.initialStateId = initialState.id;
        this.boardsize = 19;
    }

    addMove(stateId, point, color) {
        let state = this.states[stateIndex];
        if (!state) {
            throw new GoError(`State ${stateId} does not exist.`);
        }
        if (state.containsMove(point)) {
            throw new GoError(`State ${stateId} already contains a move for point ${point}.`);
        }
        let newState = new State();
        this.states[newState.id] = newState;
        let move = new Move(point, color, state.id, newState.id);
        state.moves.push(move);
        newState.movesTo.push(move);
        return move;
    }

    deleteMove(stateIndex) {

    }
}

class State {
    constructor() {
        this.id = uuidV4();
        this.comment = '';
        this.stones = [];
        this.markup = [];
        this.movesTo = [];
        this.moves = [];
        this.correct = 0;
    }

    containsMove(point) {
        return this.moves
            .map((move) => move.point.equals(point))
            .reduce((a, b) => a || b, false);
    }
}

class Move {
    constructor(point, color, stateFrom, stateTo) {
        this.point = point;
        this.color = color;
        this.stateFrom = stateFrom;
        this.stateTo = stateTo;
    }
}

class Point {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }

    equals(other) {
        return this.x === other.x && this.y === other.y;
    }
}
