import React from "react";
import {Container, Divider} from 'semantic-ui-react'
import './style.scss'

export default class ResultItem extends React.Component {

    getHighlightedText(text, highlightStart) {
        // Split on highlight term and include term into parts, ignore case
        if (text !== undefined && text.length > 0) {
            const parts = text.split(new RegExp(`(${highlightStart})`, 'gi'));
            return <span> {parts.map((part, i) =>
                                         <span key={i} style={part === highlightStart ? {
                                             fontWeight: 'bold',
                                             background: 'yellow',
                                             fontsize: 14
                                         } : {}}>
            {part}
        </span>)
            } </span>;
        } else {
            return <span>text</span>
        }
    }

    render() {
        return (
            <Container text><a href={this.props.url}>
                    {this.getHighlightedText(this.props.title, 'em>')}</a>
                <Divider horizontal/>
                <a href={this.props.url} style={{'white-space': 'pre-wrap'}}>
                    {this.getHighlightedText(this.props.highlights.content[0], 'em>')}
                </a>
                </Container>
        )
    }

}
