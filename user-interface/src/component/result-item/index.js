import React from "react";
import { Card } from 'semantic-ui-react'
import './style.scss'


export default class ResultItem extends React.Component {

    getHighlightedText(text, highlight) {
        // Split on highlight term and include term into parts, ignore case
        if( text !== undefined && text.length > 0) {
            const parts = text.split(new RegExp(`(${highlight})`, 'gi'));
            return <span> {parts.map((part, i) =>
                                         <span key={i} style={part === highlight ? {
                                             fontWeight: 'bold',
                                             background: 'yellow'
                                         } : {}}>
            {part}
        </span>)
            } </span>;
        } else{
            return  <span>text</span>
        }
    }

    render() {
        return (<Card
            className={'result-item'}
            href={this.props.url}
            header={this.props.url}
            description={this.getHighlightedText(this.props.highlights.content[0],'em>')}
            />
        )
    }

}
