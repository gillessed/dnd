import React, { Component } from 'react'
import changeCase from 'change-case'
import './WikiSpellContent.scss'

export default class extends Component {
    static propTypes = {
        spell: React.PropTypes.object.isRequired,
    }

    render() {
        return (
            <div className='spell-content-container'>
                <h3> Effect </h3>
                <p>{this.props.spell.effect}</p>
                <h3> Description </h3>
                <p>{this.props.spell.description}</p>
                <h3 style={{paddingBottom: 20}}> Properties </h3>
                {this.renderProperties()}
                {this.renderNotes()}
            </div>
        );
    }

    renderProperties() {
        const properties = this._getProperties();
        return properties.map((property) => {
            return (
                <div className='ui grid spell-property-grid'>
                    <div className='four wide column spell-property-key'>
                        {changeCase.titleCase(property.key)}:
                    </div>
                    <div className='twelve wide column spell-property-value'>
                        {property.value}
                    </div>
                </div>
            );
        });
    }

    renderNotes() {
        if (this.props.spell.notes) {
            return [
                <h3 key={0}>Notes</h3>,
                <p>{this.props.spell.notes}</p>
            ];
        }
    }

    _getProperties = () => {
        return [
            'level',
            'type',
            'typeLevel',
            'castingTime',
            'manaConsumption',
            'range',
            'area',
            'duration',
            'savingThrow',
            'spellResistance',
            'origin',
        ].map((key) => {
            return {
                key: key,
                value: this.props.spell[key]
            };
        }).filter(object => object.value);
    }
}