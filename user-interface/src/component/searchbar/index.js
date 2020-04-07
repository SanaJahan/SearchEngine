import React from 'react';
import {Input} from 'semantic-ui-react'
import SolrApi from '../solr-api/index';
import ResultPage from "../result";

export default class SearchBar extends React.Component {

    constructor(props) {
        super(props);
        this.state = {query: '', searchResult: [], ready: false};

    }

    handleChange = event => {
        if (event.key === "Enter") {
            this.setState({query: event.target.value});
            this.executeQuery().then(r => {
                console.log("success")
                this.setState({searchResult: r, ready: true})
            });
        }
    };

    async executeQuery() {
        try {
            // send the query request to the solr server through the solr api
            const solrApi = new SolrApi();
            const result = await solrApi.executeSearch(this.state.query);
            return result
        } catch (e) {
            console.error(e.message);
        }
    }

    displayResults = () => {
        if(this.state.ready === true) {
            console.log("I am about to display")
            return <ResultPage ready={this.state.ready}
                               result={this.state.searchResult}/>
        }
    }

    render() {
        return (<React.Fragment>
            <Input icon='search' focus placeholder='Search...' onKeyPress={e => this.handleChange(e)}/>
            {this.displayResults()}
            </React.Fragment>)
            }
    }


