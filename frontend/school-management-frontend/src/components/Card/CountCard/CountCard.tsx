import {
  CardContainer,
  CardTitle,
  CardValue
} from "./styles";

type Props = {
  title: string;
  value: number;
};

function CountCard({ title, value }: Props) {
  return (
    <CardContainer>
      <CardTitle>{title}</CardTitle>
      <CardValue>{value}</CardValue>
    </CardContainer>
  );
}

export default CountCard;