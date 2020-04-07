import React from "react";
import ResultItem from "../result-item";


function ResultPage(props){
    return ( props.result.map((item, key) =>
                                       <ResultItem key={item.id} url={item.url[0]} content={item.content}/>
                                       ));
}
export default ResultPage

