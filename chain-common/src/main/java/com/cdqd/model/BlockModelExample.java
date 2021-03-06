package com.cdqd.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlockModelExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BlockModelExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andBlockIndexIsNull() {
            addCriterion("block_index is null");
            return (Criteria) this;
        }

        public Criteria andBlockIndexIsNotNull() {
            addCriterion("block_index is not null");
            return (Criteria) this;
        }

        public Criteria andBlockIndexEqualTo(Integer value) {
            addCriterion("block_index =", value, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexNotEqualTo(Integer value) {
            addCriterion("block_index <>", value, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexGreaterThan(Integer value) {
            addCriterion("block_index >", value, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexGreaterThanOrEqualTo(Integer value) {
            addCriterion("block_index >=", value, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexLessThan(Integer value) {
            addCriterion("block_index <", value, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexLessThanOrEqualTo(Integer value) {
            addCriterion("block_index <=", value, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexIn(List<Integer> values) {
            addCriterion("block_index in", values, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexNotIn(List<Integer> values) {
            addCriterion("block_index not in", values, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexBetween(Integer value1, Integer value2) {
            addCriterion("block_index between", value1, value2, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andBlockIndexNotBetween(Integer value1, Integer value2) {
            addCriterion("block_index not between", value1, value2, "blockIndex");
            return (Criteria) this;
        }

        public Criteria andHashValueIsNull() {
            addCriterion("hash_value is null");
            return (Criteria) this;
        }

        public Criteria andHashValueIsNotNull() {
            addCriterion("hash_value is not null");
            return (Criteria) this;
        }

        public Criteria andHashValueEqualTo(String value) {
            addCriterion("hash_value =", value, "hashValue");
            return (Criteria) this;
        }

        public Criteria andHashValueNotEqualTo(String value) {
            addCriterion("hash_value <>", value, "hashValue");
            return (Criteria) this;
        }

        public Criteria andHashValueGreaterThan(String value) {
            addCriterion("hash_value >", value, "hashValue");
            return (Criteria) this;
        }

        public Criteria andHashValueGreaterThanOrEqualTo(String value) {
            addCriterion("hash_value >=", value, "hashValue");
            return (Criteria) this;
        }

        public Criteria andHashValueLessThan(String value) {
            addCriterion("hash_value <", value, "hashValue");
            return (Criteria) this;
        }

        public Criteria andHashValueLessThanOrEqualTo(String value) {
            addCriterion("hash_value <=", value, "hashValue");
            return (Criteria) this;
        }

        public Criteria andHashValueLike(String value) {
            addCriterion("hash_value like", value, "hashValue");
            return (Criteria) this;
        }

        public Criteria andHashValueNotLike(String value) {
            addCriterion("hash_value not like", value, "hashValue");
            return (Criteria) this;
        }

        public Criteria andHashValueIn(List<String> values) {
            addCriterion("hash_value in", values, "hashValue");
            return (Criteria) this;
        }

        public Criteria andHashValueNotIn(List<String> values) {
            addCriterion("hash_value not in", values, "hashValue");
            return (Criteria) this;
        }

        public Criteria andHashValueBetween(String value1, String value2) {
            addCriterion("hash_value between", value1, value2, "hashValue");
            return (Criteria) this;
        }

        public Criteria andHashValueNotBetween(String value1, String value2) {
            addCriterion("hash_value not between", value1, value2, "hashValue");
            return (Criteria) this;
        }

        public Criteria andPrevHashValueIsNull() {
            addCriterion("prev_hash_value is null");
            return (Criteria) this;
        }

        public Criteria andPrevHashValueIsNotNull() {
            addCriterion("prev_hash_value is not null");
            return (Criteria) this;
        }

        public Criteria andPrevHashValueEqualTo(String value) {
            addCriterion("prev_hash_value =", value, "prevHashValue");
            return (Criteria) this;
        }

        public Criteria andPrevHashValueNotEqualTo(String value) {
            addCriterion("prev_hash_value <>", value, "prevHashValue");
            return (Criteria) this;
        }

        public Criteria andPrevHashValueGreaterThan(String value) {
            addCriterion("prev_hash_value >", value, "prevHashValue");
            return (Criteria) this;
        }

        public Criteria andPrevHashValueGreaterThanOrEqualTo(String value) {
            addCriterion("prev_hash_value >=", value, "prevHashValue");
            return (Criteria) this;
        }

        public Criteria andPrevHashValueLessThan(String value) {
            addCriterion("prev_hash_value <", value, "prevHashValue");
            return (Criteria) this;
        }

        public Criteria andPrevHashValueLessThanOrEqualTo(String value) {
            addCriterion("prev_hash_value <=", value, "prevHashValue");
            return (Criteria) this;
        }

        public Criteria andPrevHashValueLike(String value) {
            addCriterion("prev_hash_value like", value, "prevHashValue");
            return (Criteria) this;
        }

        public Criteria andPrevHashValueNotLike(String value) {
            addCriterion("prev_hash_value not like", value, "prevHashValue");
            return (Criteria) this;
        }

        public Criteria andPrevHashValueIn(List<String> values) {
            addCriterion("prev_hash_value in", values, "prevHashValue");
            return (Criteria) this;
        }

        public Criteria andPrevHashValueNotIn(List<String> values) {
            addCriterion("prev_hash_value not in", values, "prevHashValue");
            return (Criteria) this;
        }

        public Criteria andPrevHashValueBetween(String value1, String value2) {
            addCriterion("prev_hash_value between", value1, value2, "prevHashValue");
            return (Criteria) this;
        }

        public Criteria andPrevHashValueNotBetween(String value1, String value2) {
            addCriterion("prev_hash_value not between", value1, value2, "prevHashValue");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}