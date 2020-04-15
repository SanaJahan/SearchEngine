import React from "react";
import ResultItem from "../result-item";
import List from "semantic-ui-react/dist/commonjs/elements/List";

function ResultPage(props) {
    return (
        props.result.map((item, key) => <List className={'result-item'}>
                             <ResultItem key={item.id} url={item.url} title={item.title} content={item.content}
                                         highlights={props.highlights[item.id]}/>
                         </List>
        ));
}

export default ResultPage

