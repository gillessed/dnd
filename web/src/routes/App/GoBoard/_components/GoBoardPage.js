import React, { Component } from 'react'
import GoBoard from './GoBoard'

class GoBoardPage extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        this.objectNumber = 0;
        return (
            <div style={{ margin: '0 auto'}}>
                <h1>GoBoard</h1>
                <GoBoard
                    rootId={'goBoard'}
                    size={600}
                    boardSize={19}/>
            </div>
        );
    }

}

export default GoBoardPage;
