import axios from "axios";

function getPayload(query) {
            const
                activePage = query.page,
                page = activePage > 0 ? activePage - 1 : 0,
                size = query.size,
                start = page * size;
            const payload = {
                params: {
                    start: start,
                    hl:"on",
                    "hl.fl":"content",
                    "hl.requireFieldMatch":"true",
                    "usePhraseHighLighter":"true",
                    "highlightMultiTerm":"true",
                    q: 'content:'+query.query+' Or url:'+query.query,
                    wt:"json"
                }
            };
            return payload;
}

export default class SolrApi{
    constructor() {
        this.url = "http://localhost:8983/solr/travelandeat/tneselect";
    }

    // hit the solr localhost select and get the result in json format here.
    executeSearch = async (query) => {
        try {
            const request = getPayload(query)
            const response = await axios.get(this.url, request);
            const data = response;
            const serializedResponse = this.serialize(data);
            return serializedResponse;
        } catch (error) {
            console.error(error.message);
        }
    }
    //send the result to the result component

    serialize(data) {
        let response = data.data.response
        return {
            result: response.docs,
            total: response.numFound,
            start: response.start,
            highlights: data.data.highlighting,
            queryTime: data.data.responseHeader.QTime
        }
    }
}
