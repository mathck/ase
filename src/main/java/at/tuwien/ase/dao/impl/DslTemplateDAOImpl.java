package at.tuwien.ase.dao.impl;

import at.tuwien.ase.dao.DslTemplateDAO;
import at.tuwien.ase.model.DslTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by DanielHofer on 21.12.2015.
 */

@Repository
public class DslTemplateDAOImpl implements DslTemplateDAO {

    private static final Logger logger = LogManager.getLogger(DslTemplateDAOImpl.class);

    private JdbcTemplate jdbcTemplate;
    KeyHolder keyHolder;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.keyHolder = new GeneratedKeyHolder();
    }


    public void insertDslTemplate(DslTemplate template) {

        logger.debug("insert into db: dsl template with id=" + template.getId());

        String sqlQuery = "INSERT INTO DSL_TEMPLATE (ID, TITLE, DESCRIPTION, SYNTAX, CREATION_DATE, TEMPLATE_CATEGORY_NAME, TEMPLATE_CATEGORY_DESCRIPTION) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(
                sqlQuery,
                template.getId(),
                template.getTitle(),
                template.getDescription(),
                template.getSyntax(),
                template.getCreationDate(),
                template.getTemplateCategoryName(),
                template.getTemplateCategoryDescription());

    }

    public void removeDslTemplateByID(int tID) {

        logger.debug("delete from db: dsl template with id=" + tID);

        String sqlQuery = "DELETE " +
                "FROM DSL_TEMPLATE " +
                "WHERE ID = ? ";

        this.jdbcTemplate.update(
                sqlQuery,
                tID
        );
    }

    public DslTemplate findByID(int tID) {

        logger.debug("retrieve from db: dsl template with id=" + tID);

        String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, SYNTAX, CREATION_DATE, TEMPLATE_CATEGORY_NAME, TEMPLATE_CATEGORY_DESCRIPTION " +
                "FROM DSL_TEMPLATE " +
                "WHERE ID = ? ";

        return this.jdbcTemplate.queryForObject(
                sqlQuery,
                new Object[]{tID},
                new RowMapper<DslTemplate>() {
                    public DslTemplate mapRow(ResultSet rs, int taskId) throws SQLException {
                        DslTemplate template = new DslTemplate();
                        template.setId(Integer.valueOf(rs.getString("id")));
                        template.setTitle(rs.getString("title"));
                        template.setDescription(rs.getString("description"));
                        template.setSyntax(rs.getString("syntax"));
                        template.setCreationDate(rs.getDate("creation_date"));
                        template.setTemplateCategoryName(rs.getString("template_category_name"));
                        template.setTemplateCategoryDescription(rs.getString("template_category_description"));
                        return template;
                    }
                });
    }

    public LinkedList<DslTemplate> loadAll() {

        logger.debug("retrieve from db: all dsl templates");

        String sqlQuery = "SELECT ID, TITLE, DESCRIPTION, SYNTAX, CREATION_DATE, TEMPLATE_CATEGORY_NAME, TEMPLATE_CATEGORY_DESCRIPTION " +
                "FROM DSL_TEMPLATE ";

        LinkedList<DslTemplate> templates = new LinkedList<DslTemplate>();

        List<Map<String,Object>> rows =  this.jdbcTemplate.queryForList(sqlQuery);
        for (Map<String,Object> row : rows) {

            DslTemplate template = new DslTemplate();

            template.setId((Integer)row.get("id"));
            template.setTitle((String)row.get("title"));
            template.setDescription((String)row.get("description"));
            template.setSyntax((String)row.get("syntax"));
            template.setCreationDate(new java.sql.Date(((Timestamp)row.get("creation_date")).getTime()));
            template.setTemplateCategoryName((String)row.get("template_category_name"));
            template.setTemplateCategoryDescription((String)row.get("template_category_description"));

            templates.add(template);
        }

        return templates;
    }

    public void alterDslTemplateByID(DslTemplate template, int tID) {

        logger.debug("update dsl template with id="+tID);

        String sqlQuery = "UPDATE DSL_TEMPLATE " +
                "SET TITLE = ?, DESCRIPTION = ?, SYNTAX = ?, TEMPLATE_CATEGORY_NAME = ?, TEMPLATE_CATEGORY_DESCRIPTION = ? " +
                "WHERE ID = ?";

        this.jdbcTemplate.update(
                sqlQuery,
                template.getTitle(),
                template.getDescription(),
                template.getSyntax(),
                template.getTemplateCategoryName(),
                template.getTemplateCategoryDescription(),
                tID
        );

    }

    public int getNewID() {

        Integer id = this.jdbcTemplate.queryForObject(
                "SELECT nextval('seq_dsl_template_id')",
                Integer.class);

        return id;
    }
}
