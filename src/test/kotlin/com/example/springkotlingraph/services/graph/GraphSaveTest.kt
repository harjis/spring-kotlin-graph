package com.example.springkotlingraph.services.graph

import com.example.springkotlingraph.app.services.graph.GraphSave
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class GraphSaveTest (
        @Autowired val graphSave: GraphSave
) {

}
