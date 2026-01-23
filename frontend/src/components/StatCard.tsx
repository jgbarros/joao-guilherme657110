import { Card } from 'primereact/card';
import 'primeicons/primeicons.css';
import './StatCard.css';

interface StatCardProps {
    title?: string;
    value?: number | string;
    icon?: string;
    color?: string;
}

export default function StatCard({ title, value, icon, color }: StatCardProps) {
  return (
    <Card className="stat-card">
      <div className="stat-header">
        <span className="stat-title">{title}</span>
        <div className="stat-icon"
             style={{ width: 40, height: 40, background: color }}>
          <i className={`pi ${icon} text-white`}></i>
        </div>
      </div>

      <div className="stat-value">{value}</div>
    </Card>
  );
}