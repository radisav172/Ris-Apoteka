import styles from './FormField.module.css'

export default function FormField({ label, name, type = 'text', value, onChange, options, required }) {
  return (
    <div className={styles.field}>
      <label htmlFor={name}>{label}{required && ' *'}</label>
      {options ? (
        <select id={name} name={name} value={value} onChange={onChange} required={required}>
          <option value="">— izaberi —</option>
          {options.map(o => (
            <option key={o.value} value={o.value}>{o.label}</option>
          ))}
        </select>
      ) : (
        <input
          id={name}
          name={name}
          type={type}
          value={value}
          onChange={onChange}
          required={required}
        />
      )}
    </div>
  )
}
