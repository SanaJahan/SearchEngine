import React from "react";
import { Card } from 'semantic-ui-react'
import './style.scss'


export default class ResultItem extends React.Component {


    render() {
        return (<Card
            className={'result-item'}
            href={this.props.url}
            header={this.props.url}
            description={this.props.highlights.content[0]}
        />)
    }

}
// TODO:
// 1. Trim the content. Make the result more presentable
// 2. add more proper stopwords. fine tune the query as asked in the email. Make sure the query goes with content:query-done
//  OR url: query. See what has to be done around that. Because the results being shown right now are default ones-done
// 3. highlight the match indexes. Solr gives the option to do that
// 4. Pagination for all the results. Right now only 10 results are being shown. Solr has API for that as well.
// 5. crawl the food section and commit to the solr index

