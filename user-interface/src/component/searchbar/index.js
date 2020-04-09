import React from 'react';
import SolrApi from '../solr-api/index';
import ResultPage from "../result";
import {Pagination, Segment, Card, Input} from "semantic-ui-react";

export default class SearchBar extends React.Component {

    constructor(props) {
        super(props);
        this.state =
            {query: '', searchResult: [], ready: false, current_page: 1, solrApi: new SolrApi()};

    }

    handlePaginationChange = async (e, {activePage}) => {
        try {
            const data = await this.state.solrApi.executeSearch({
                                                             query: this.state.query,
                                                             page: activePage,
                                                             size: 10
                                                         });
            this.setState({searchResult: data, current_page: activePage});
        } catch (e) {
            console.error(e.message);
        }
    };

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
            const result = await this.state.solrApi.executeSearch({
                                                                      query: q,
                                                                      page: this.state.current_page,
                                                                      size: 10
                                                                  });
            return result
        } catch (e) {
            console.error(e.message);
        }
    }

    displayResults = (searchResults) => {
        if (this.state.ready === true && searchResults !== undefined && searchResults.result.length > 0) {
            const totalPages = Math.ceil(searchResults.total / 10)
            return (<div><ResultPage ready={this.state.ready}
                                     result={searchResults.result}
                                     highlights={searchResults.highlights}/>
                <Segment>
                    {this.state.ready === true && searchResults.result && searchResults.total >= 0
                     ? (
                         <div>
                             <Segment textAlign="center" vertical>
                                 <Pagination
                                     activePage={this.state.current_page}
                                     onPageChange={this.handlePaginationChange}
                                     totalPages={totalPages}
                                 />
                             </Segment>
                         </div>) : (<React.Fragment/>)
                    }
                </Segment>
            </div>)
        } else if (this.state.ready === true && (searchResults === undefined || searchResults.result.length < 1)) {
            return <Card
                className={'result-item'}
                header="No Results for this query"
                description="Search again"
            />
        }
    }

    render() {
        return (<React.Fragment>
            <Input icon='search' focus placeholder='Search...'
                   onKeyPress={e => this.handleChange(e)}/>
            {this.displayResults(this.state.searchResult)}
        </React.Fragment>)
    }
}


