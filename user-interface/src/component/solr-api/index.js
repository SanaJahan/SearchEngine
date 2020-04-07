import React from "react";
import axios from "axios";

export default class SolrApi{
    constructor() {
        this.url = "http://localhost:8983/solr/travelandeat/select?q=*%3A*";
    }

    // hit the solr localhost select and get the result in json format here.
    executeSearch = async (query) => {
        try {
            const response = await axios.get(this.url, query);
            const data = response.data;
            return data.response.docs;
        } catch (error) {
            console.error(error.message);
        }
    }
    //send the result to the result component



}
