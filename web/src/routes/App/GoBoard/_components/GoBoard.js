import React, { Component } from 'react'
import { Link } from 'react-router'

class GoBoardPage extends Component {
    static propTypes = {
        rootId: React.PropTypes.string.isRequired,
        size: React.PropTypes.number.isRequired,
        boardSize: React.PropTypes.number.optional,
        margin: React.PropTypes.number.optional,
    };

    static defaultProps = {
        boardSize: 19,
        margin: 10,
    };

    constructor(props) {
        super(props);
        this.scale = 1 / this.props.size;
        this.cellSize = 1 / this.props.boardSize;
        this.fullSize = this.props.size + 2 * this.props.margin;
        this.indexRange = [];
        this.starPoints = this._calculateStarPointArray();
        for (let i = 0; i < this.props.boardSize; i++) {
            this.indexRange.push(i);
        }
    }


    render() {
        return (
            <svg
                id={this.props.rootID}
                width={this.fullSize}
                height={this.fullSize}>
                <rect cx='0' cy='0' width={this.fullSize} height={this.fullSize} stroke='black' strokeWidth={this.scale} fill='rgb(255,204,0)'/>
                <g transform={`translate(${this.props.margin},${this.props.margin})`}>
                    <g transform={`scale(${this.props.size}, ${this.props.size})`}>
                        {this.indexRange.map(this.renderVerticalLine)}
                        {this.indexRange.map(this.renderHorizontalLine)}
                        {this.starPoints.map(this.renderStarPoint)}
                    </g>
                </g>
            </svg>
        );
    }

    renderVerticalLine = (index) => {
        const translation = 1 / this.props.boardSize * (index + 0.5);
        return <line x1='0' y1={this.cellSize / 2} x2='0' y2={1 - this.cellSize / 2} strokeWidth={this.scale} stroke='black' transform={`translate(${translation}, 0)`}/>;
    }

    renderHorizontalLine = (index) => {
        const translation = 1 / this.props.boardSize * (index + 0.5);
        return <line x1={this.cellSize / 2} y1='0' x2={1 - this.cellSize / 2} y2='0' strokeWidth={this.scale} stroke='black' transform={`translate(0, ${translation})`}/>;
    }

    renderStarPoint = (point) => {
        const tx = 1 / this.props.boardSize * (point[0] + 0.5);
        const ty = 1 / this.props.boardSize * (point[1] + 0.5);
        const scale = this.scale * 4;
        return <circle cx='0' cy='0' r={scale} transform={`translate(${tx}, ${ty})`}/>;
    }

    _calculateStarPointArray() {
        const indices = this._calculateStarPointIndices();
        const mapping = indices.map((index1) => {
            return indices.map((index2) => {
                return [index1, index2];
            });
        });
        return mapping.reduce((subList, acc) => {
            return acc.concat(subList);
        }, []);
    }

    _calculateStarPointIndices() {
        if (this.props.boardSize % 2 == 1) {
            if (this.props.boardSize > 2 && this.props.boardSize < 9) {
                return [Math.floor(this.props.boardSize / 2)];
            } else if (this.props.boardSize >= 9 && this.props.boardSize < 13) {
                return [
                    2,
                    Math.floor(this.props.boardSize / 2),
                    this.props.boardSize - 3
                ];
            } else {
                return [
                    3,
                    Math.floor(this.props.boardSize / 2),
                    this.props.boardSize - 4
                ];
            }
        } else {
            return [];
        }
    }
}

export default GoBoardPage;
