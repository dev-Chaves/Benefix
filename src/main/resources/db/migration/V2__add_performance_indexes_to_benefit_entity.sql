CREATE INDEX idx_benefit_supplier_enterprise ON benefit_entity(supplier_enterprise_id);

CREATE INDEX idx_benefit_category_supplier ON benefit_entity(category, supplier_enterprise_id);