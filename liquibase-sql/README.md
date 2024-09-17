# Liquibase updater




## 🎯 Convert to SQL changeset

```bash
liquibase --classpath=target/classes/ update-sql --changelog-file src/main/resources/db/changelog/db.changelog-master.yaml
```

## 💦 Tag existing database

```bash
liquibase --classpath=target/classes/ changelog-sync-to-tag --tag=3.2.2 --changelog-file src/main/resources/db/changelog/db.changelog-master.yaml
```