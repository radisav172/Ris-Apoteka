import styles from './PageTable.module.css'

export default function PageTable({ columns, rows, onEdit, onDelete }) {
  return (
    <div className={styles.wrap}>
      <table className={styles.table}>
        <thead>
          <tr>
            {columns.map(c => <th key={c.key}>{c.label}</th>)}
            {(onEdit || onDelete) && <th className={styles.actions}>Akcije</th>}
          </tr>
        </thead>
        <tbody>
          {rows.length === 0 ? (
            <tr><td colSpan={columns.length + 1} className={styles.empty}>Nema podataka</td></tr>
          ) : rows.map((row, i) => (
            <tr key={row.id ?? i}>
              {columns.map(c => (
                <td key={c.key}>{c.render ? c.render(row) : row[c.key]}</td>
              ))}
              {(onEdit || onDelete) && (
                <td className={styles.actions}>
                  {onEdit && <button className={styles.edit} onClick={() => onEdit(row)}>Izmeni</button>}
                  {onDelete && <button className={styles.delete} onClick={() => onDelete(row)}>Obrisi</button>}
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
