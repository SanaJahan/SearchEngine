import React from "react";
import { Grid, It } from 'semantic-ui-react'
import './style.scss'
import List from "semantic-ui-react/dist/commonjs/elements/List";
import Segment from "semantic-ui-react/dist/commonjs/elements/Segment";


export default class ResultItem extends React.Component {

    getHighlightedText(text, highlight) {
        // Split on highlight term and include term into parts, ignore case
        if( text !== undefined && text.length > 0) {
            const parts = text.split(new RegExp(`(${highlight})`, 'gi'));
            return <span> {parts.map((part, i) =>
                                         <span key={i} style={part === highlight ? {
                                             fontWeight: 'bold',
                                             background: 'yellow',
                                             fontsize: 14
                                         } : {}}>
            {part}
        </span>)
            } </span>;
        } else{
            return  <span>text</span>
        }
    }

    render() {
        return (
                <List.Item>
                <List.Content>
                    <List.Header as='a'><a href={this.props.url}>
                        {this.getHighlightedText(this.props.title, 'em>')}</a>
                    </List.Header>
                    <List.Description>
                        <Segment textAlign='center' className={'text-style'}>
                            <a href={this.props.url}>
                                {this.getHighlightedText(this.props.highlights.content[0],'em>')}
                            </a>
                        </Segment>
                    </List.Description>
                </List.Content>
            </List.Item>
        )
    }

}
