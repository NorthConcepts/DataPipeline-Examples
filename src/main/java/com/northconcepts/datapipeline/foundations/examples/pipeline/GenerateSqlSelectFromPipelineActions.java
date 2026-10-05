package com.northconcepts.datapipeline.foundations.examples.pipeline;

import com.northconcepts.datapipeline.foundations.pipeline.action.aggregate.AggregateGroupFieldsAction;
import com.northconcepts.datapipeline.foundations.pipeline.action.aggregate.AggregateGroupFieldsAction.GroupByField;
import com.northconcepts.datapipeline.foundations.pipeline.action.aggregate.AggregateGroupFieldsAction.OperatorType;
import com.northconcepts.datapipeline.foundations.pipeline.action.transform.RenameFieldsAction;
import com.northconcepts.datapipeline.foundations.pipeline.action.transform.SortFieldsAction;
import com.northconcepts.datapipeline.jdbc.sql.select.Select;
import com.northconcepts.datapipeline.jdbc.sql.select.TableQuerySource;

public class GenerateSqlSelectFromPipelineActions {

    public static void main(String[] args) {
        AggregateGroupFieldsAction aggregate = new AggregateGroupFieldsAction()
                .add(new GroupByField("Rating", "Rating", OperatorType.GROUPBY, true))
                .add(new GroupByField("Account", "Accounts", OperatorType.COUNT, true))
                .add(new GroupByField("Balance", "TotalBalance", OperatorType.SUM, true));

        RenameFieldsAction rename = new RenameFieldsAction()
                .add("Rating", "CreditRating")
                .add("Accounts", "AccountCount")
                .add("TotalBalance", "TotalBalance");

        SortFieldsAction sort = new SortFieldsAction()
                .add("TotalBalance", false);

        Select select = aggregate.generateSqlSelect(new TableQuerySource("credit_balance"));
        select = rename.generateSqlSelect(select.setNestedAlias("totals"));
        select = sort.generateSqlSelect(select.setNestedAlias("renamed"));

        System.out.println(select.setPretty(true).getSqlFragment());
    }

}
