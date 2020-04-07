import React from 'react';
import "semantic-ui-css/semantic.min.css";
import './App.css';
import SearchBar from './component/searchbar'

export default class App extends React.Component {



    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <SearchBar name='search here' value={this.props.query}  onChange={e => this.handleChange(e)} />
                </header>
            </div>
        );
    }
}
