import React from 'react';

import ProductCard from '../../../components/Card';

import './ProductOffers.scss';

const ProductOffers = () => {
	return (
		<div className = 'offersBoxes'>
			<ProductCard
				city = { 'Харьков' }
				text = { `Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет` }
				picture = { 'https://static.toiimg.com/photo/72975551.cms' }
			/>
			<ProductCard
				city = { 'Харьков' }
				text = { `Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет` }
				picture = { 'https://static.toiimg.com/photo/72975551.cms' }
			/>
			<ProductCard
				city = { 'Харьков' }
				text = { `Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет` }
				picture = { 'https://static.toiimg.com/photo/72975551.cms' }
			/>
			<ProductCard
				city = { 'Харьков' }
				text = { `Велосипед ну очень куртой. просто не реально крутой для девочки 5 лет` }
				picture = { 'https://static.toiimg.com/photo/72975551.cms' }
			/>
		</div>
	);
};
export default ProductOffers;