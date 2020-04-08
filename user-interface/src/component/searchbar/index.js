import React from 'react';
import {Card, Input} from 'semantic-ui-react'
import SolrApi from '../solr-api/index';
import ResultPage from "../result";

export default class SearchBar extends React.Component {

    constructor(props) {
        super(props);
        this.state = {query: '', searchResult: [], ready: false};

    }


    handleChange = async event => {
        if (event.key === "Enter") {
            const q = event.target.value
            this.setState({query: event.target.value})
            this.executeQuery(q).then(r => { // BUG: still need to double click to update state
                this.setState({searchResult: r, ready: true})
            });
        }
    };

    async executeQuery(q) {
        try {
            // send the query request to the solr server through the solr api
            const solrApi = new SolrApi();
            const result = await solrApi.executeSearch({query: q,page: 1,
                                                           size: 10});
            return result
        } catch (e) {
            console.error(e.message);
        }
    }

    displayResults = () => {
        if(this.state.ready === true && this.state.searchResult !== undefined) {
            return (<ResultPage ready={this.state.ready}
                               result={this.state.searchResult.result} highlights={this.state.searchResult.highlights}/>)
        }else if(this.state.ready === true && this.state.searchResult === undefined){
            return <Card
                className={'result-item'}
                header="No Results for this query"
                description="Search again"
            />
        }
    }

    render() {
        return (<React.Fragment>
            <Input icon='search' focus placeholder='Search...' onKeyPress={e => this.handleChange(e)}/>
            {this.displayResults()}
            </React.Fragment>)
            }
    }


