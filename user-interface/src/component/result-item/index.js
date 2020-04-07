import React from "react";
import { Card } from 'semantic-ui-react'
import './style.scss'

const ResultItem = (props) => (
    <Card
        className={'result-item'}
        href={props.url}
        header={props.url}
        description={props.content}
    />
)

export default ResultItem

// TODO:
// 1. Trim the content. Make the result more presentable
// 2. add more proper stopwords. fine tune the query as asked in the email. Make sure the query goes with content:query
//  OR url: query. See what has to be done around that. Because the results being shown right now are default ones
// 3. highlight the match indexes. Solr gives the option to do that
// 4. Pagination for all the results. Right now only 10 results are being shown. Solr has API for that as well.
// 5. crawl the food section and commit to the solr index

